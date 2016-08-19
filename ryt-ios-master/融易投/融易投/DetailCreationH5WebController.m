//
//  DetailCreationH5WebController.m
//  融易投
//
//  Created by efeiyi on 16/5/30.
//  Copyright © 2016年 融艺投. All rights reserved.
//

#import "DetailCreationH5WebController.h"
#import "NSObject+Extension.h"
#import "PageInfoModel.h"
#import "CreationModel.h"
#import <MJExtension.h>
#import "ArtistUserHomeViewController.h"
#import "CommonUserHomeViewController.h"
#import "PostCommentController.h"
#import "investmentController.h"
#import "FinanceFooterView.h"

@interface DetailCreationH5WebController () <UIWebViewDelegate,FinanceFooterViewDelegate>
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@end

@implementation DetailCreationH5WebController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.navigationController.navigationBarHidden = NO;
    [self setupWebView];
    [self setupFooterView];
}
-(void)setupFooterView{
    
    FinanceFooterView *footerView= [FinanceFooterView FinanceFooterView];
    footerView.widthConstraint.constant = 0;
    footerView.delegate = self;
    footerView.frame = CGRectMake(0, CGRectGetMaxY(self.webView.frame), SSScreenW, 49);
    [self.view addSubview:footerView];
}
-(void)setupWebView{
    
    NSURL *url = [[NSBundle mainBundle] URLForResource:@"sH5/A2.html" withExtension:nil];
    [self.webView loadRequest:[NSURLRequest requestWithURL:url]];
    
    //让webView 自适应
    self.webView.scalesPageToFit = YES;

    
    //设置webView的代理
    self.webView.delegate = self;
}
#pragma mark -<UIWebViewDelegate>
- (void)webViewDidFinishLoad:(UIWebView *)webView{
    
    UserMyModel *model = TakeLoginUserModel;
    NSString *currentUserId = model.ID;
    NSString *artWorkId = self.artWorkId;
    NSString *timestamp = [MyMD5 timestamp];
    NSString *appkey = MD5key;
    NSString *signmsg = [NSString stringWithFormat:@"artWorkId=%@&currentUserId=%@&timestamp=%@&key=%@",artWorkId,currentUserId,timestamp,appkey];
    NSString *signmsgMD5 = [MyMD5 md5:signmsg];
    
    /*
    //方式一:给JS传递参数
    NSDictionary *json = @{
                           @"currentUserId" : currentUserId,
                           @"artWorkId" : artWorkId,
                           @"timestamp" : timestamp,
                           @"signmsg"   : signmsgMD5
                           };
    NSString *js1 = [NSString stringWithFormat:@"getParamObject1('%@')",json];
    [self.webView stringByEvaluatingJavaScriptFromString:js1];
    */
    
    //方式二:给JS传递参数
    NSString *js2 = [NSString stringWithFormat:@"initPage('%@','%@','%@','%@');",artWorkId,currentUserId,signmsgMD5,timestamp];
    [self.webView stringByEvaluatingJavaScriptFromString:js2];
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSLog(@"shouldStartLoadWithRequest");
    
    //当点击按钮的时候会调到这个方法
    NSLog(@"%@",request.URL.absoluteString);
    
    NSString *urlStr = request.URL.absoluteString;
    //rongyitou://jumpToUserHome_?skldjflksdjflk
    
    NSString *format = @"rongyitou://";
    if ([urlStr hasPrefix:format]) {
        NSString *str = [urlStr substringFromIndex:format.length];
        NSArray *subStrArray = [str componentsSeparatedByString:@"?"];
        NSLog(@"%@",subStrArray);
        /*
         (
         "jumpToUserHome_",
         skldjflksdjflk
         )
         */
        
        NSString *methodStr = [[subStrArray firstObject] stringByReplacingOccurrencesOfString:@"_" withString:@":"];
        
        //把方法名封装成方法
        SEL selector = NSSelectorFromString(methodStr);
        
        NSString *paramStr = [subStrArray lastObject];
        NSLog(@"%@",paramStr);
        
        //skldjflksdjflk
        
        //分类中可以传递多个参数,这里不需要,因为主需要传递一个参数
//        [self performSelector:selector withObjects:subParamArray];
        [self performSelector:selector withObject:paramStr withObject:nil];
        
        return NO;
    }
    
    return YES;
}

-(void)jumpToUserHome:(NSString *)userId
{
    NSLog(@"跳转到%@主页",userId);
    
    // 3.设置请求体
    NSDictionary *json = @{
                           @"userId":userId
                           };
    
    NSString *url = @"user.do";
    
    [[HttpRequstTool shareInstance] loadData:POST serverUrl:url parameters:json showHUDView:nil andBlock:^(id respondObj) {
        
        NSString *jsonStr=[[NSString alloc] initWithData:respondObj encoding:NSUTF8StringEncoding];
        NSLog(@"返回结果:%@",jsonStr);
        
        NSDictionary *modelDict = [NSJSONSerialization JSONObjectWithData:respondObj options:kNilOptions error:nil];
        
        PageInfoModel *model = [PageInfoModel mj_objectWithKeyValues:modelDict[@"data"]];
        
        //保存模型,赋值给控制器
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            //艺术家用户
            if (model.user.master) {
                
                ArtistUserHomeViewController *artistHomeVC = [[ArtistUserHomeViewController alloc] init];
                
                artistHomeVC.userId = userId;
                artistHomeVC.title = model.user.name;
                
                [self.navigationController pushViewController:artistHomeVC animated:YES];
                
            }else{ //普通用户
                
                CommonUserHomeViewController *myHomeVC = [[CommonUserHomeViewController alloc] init];
                
                myHomeVC.userId = userId;
                myHomeVC.title = model.user.name;
                
                [self.navigationController pushViewController:myHomeVC animated:YES];
            }
            
        }];
        
    }];
    
}

//跳转到评论页面
-(void)jumpPLController{
    RYTLoginManager *manager =  [RYTLoginManager shareInstance];
    if ([manager showLoginViewIfNeed]) {
    }else{
        PostCommentController * postComment = [[PostCommentController alloc] init];
        postComment.title = @"评论";
        postComment.artworkId = self.artWorkId;
        postComment.currentUserId = [manager takeUser].ID;
        [self.navigationController pushViewController:postComment animated:YES];
    }
}

//点赞
-(void)clickZan:(UIButton *)zan{
    RYTLoginManager *manager =  [RYTLoginManager shareInstance];
    if ([manager showLoginViewIfNeed]) {
    }else{
        NSString *userId = [[RYTLoginManager shareInstance] takeUser].ID;
        NSString *urlStr = @"artworkPraise.do";
        NSDictionary *json = @{
                               @"artworkId" : self.artWorkId,
                               @"currentUserId": userId,
                               };
        [[HttpRequstTool shareInstance] loadData:POST serverUrl:urlStr parameters:json showHUDView:self.view andBlock:^(id respondObj) {
            NSString *jsonStr=[[NSString alloc] initWithData:respondObj encoding:NSUTF8StringEncoding];
            NSLog(@"返回结果:%@",jsonStr);
            NSDictionary *modelDict = [NSJSONSerialization JSONObjectWithData:respondObj options:kNilOptions error:nil];
            NSString *str = modelDict[@"resultMsg"];
            if ([str isEqualToString:@"成功"]){
                [self addNumToZan:zan];
            }
        }];
    }
}
-(void)addNumToZan:(UIButton *)zan{
    UILabel *numLabel = [[UILabel alloc] initWithFrame:zan.frame];
    numLabel.center = zan.center;
    numLabel.textAlignment = NSTextAlignmentCenter;
    numLabel.text = @"+1";
    numLabel.textColor = [UIColor colorWithRed:1.0 green:0 blue:0 alpha:0.7];
    [zan addSubview:numLabel];
    [UIView animateWithDuration:0.6 animations:^{
        CGFloat x = numLabel.centerX;
        CGPoint p = CGPointMake(x, 0);
        numLabel.center = p;
        numLabel.alpha = 0;
    } completion:^(BOOL finished) {
        [numLabel removeFromSuperview];
    }];
    NSString *zanNum = [NSString stringWithFormat:@" %ld",self.model.praiseNUm + 1];
    [zan setTitle:zanNum forState:(UIControlStateNormal)];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];

}



@end
