//
//  WalletFooterView.m
//  融易投
//
//  Created by efeiyi on 16/5/4.
//  Copyright © 2016年 dongxin. All rights reserved.
//

#import "WalletFooterView.h"
#import "CommonButton.h"

@implementation WalletFooterView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)btnClick:(CommonButton *)sender {
    
    if (_checkMoreBillBlcok != nil) {
        _checkMoreBillBlcok();
    }
}

@end
