package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by  on 2016/4/5.
 */
public class RongZi implements Serializable {
    /**
     * id : qydeyugqqiugd7
     * title : 测试6
     * brief : 这是一个
     * description : null
     * status : 1
     * investGoalMoney : 1
     * investStartDatetime : 1455005261000
     * investEndDatetime : 1454314064000
     * auctionStartDatetime : 1454400455000
     * auctionEndDatetime : 1462003649000
     * author : {"id":"icjxkedl0000b6i0","username":"123123","name":"魏立中","pictureUrl":"http://tenant.efeiyi.com/background/蔡水况.jpg","cityId":null,"status":"0","createDatetime":null,"type":"10000","master":{"id":"icjxkedl0000b6i0","brief":"版画家，他使得业已消失数百年的明代印刷业老字号十竹斋重新恢复并焕发生机，成为杭州市文化产业传承创新的亮点。","title":"国家级传承人","favicon":"http://tenant.efeiyi.com/background/蔡水况.jpg","birthday":"1968年","level":"1","content":null,"presentAddress":"浙江","backgroundUrl":"background/魏立中.jpg","provinceName":"浙江","theStatus":"1","logoUrl":"logo/魏立中.jpg","masterSpeech":null,"artCategory":null,"titleCertificate":null,"feedback":null,"identityFront":null,"identityBack":null}}
     * createDatetime : 1454314046000
     * picture_url : http://tenant.efeiyi.com/background/蔡水况.jpg
     * step : 33
     * investsMoney : 0
     * creationEndDatetime : 1458285492000
     * type : 3
     * newCreationDate : null
     * auctionNum : null
     * newBidingPrice : 500
     * newBiddingDate : null
     * sorts : 6
     * winner : null
     * feedback : null
     * duration : null
     * startingPrice : 1000
     */
    private long investRestTime;
     //       private String investRestTime;
    private String id;
    private  boolean praise;
    private String title;
    private String brief;
    private Object description;
    private String status;
    private BigDecimal investGoalMoney;
    private long investStartDatetime;
    private long investEndDatetime;
    private long auctionStartDatetime;
    private long auctionEndDatetime;
    /**
     * id : icjxkedl0000b6i0
     * username : 123123
     * name : 魏立中
     * pictureUrl : http://tenant.efeiyi.com/background/蔡水况.jpg
     * cityId : null
     * status : 0
     * createDatetime : null
     * type : 10000
     * master : {"id":"icjxkedl0000b6i0","brief":"版画家，他使得业已消失数百年的明代印刷业老字号十竹斋重新恢复并焕发生机，成为杭州市文化产业传承创新的亮点。","title":"国家级传承人","favicon":"http://tenant.efeiyi.com/background/蔡水况.jpg","birthday":"1968年","level":"1","content":null,"presentAddress":"浙江","backgroundUrl":"background/魏立中.jpg","provinceName":"浙江","theStatus":"1","logoUrl":"logo/魏立中.jpg","masterSpeech":null,"artCategory":null,"titleCertificate":null,"feedback":null,"identityFront":null,"identityBack":null}
     */

    private AuthorBean author;
    private long createDatetime;
    private String picture_url;
    private String step;
    private BigDecimal investsMoney;
    private long creationEndDatetime;
    private String type;
    private long newCreationDate;
    private int auctionNum;
    private int newBidingPrice;
    private Object newBiddingDate;
    private String sorts;
    private User winner;
    private Object feedback;
    private Object duration;
    private int startingPrice;
    private int investNum;
    private int praiseNUm;
    public int getInvestNum() {
        return investNum;
    }

    public int getPraiseNUm() {
        return praiseNUm;
    }

    public void setPraiseNUm(int praiseNUm) {
        this.praiseNUm = praiseNUm;
    }

    public void setInvestNum(int investNum) {
        this.investNum = investNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public long getInvestRestTime() {
        return investRestTime;
    }

    public void setInvestRestTime(long investRestTime) {
        this.investRestTime = investRestTime;
    }

    /*public String getInvestRestTime() {
        return investRestTime;
    }

    public void setInvestRestTime(String investRestTime) {
        this.investRestTime = investRestTime;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public long getInvestStartDatetime() {
        return investStartDatetime;
    }

    public void setInvestStartDatetime(long investStartDatetime) {
        this.investStartDatetime = investStartDatetime;
    }

    public long getInvestEndDatetime() {
        return investEndDatetime;
    }

    public void setInvestEndDatetime(long investEndDatetime) {
        this.investEndDatetime = investEndDatetime;
    }

    public long getAuctionStartDatetime() {
        return auctionStartDatetime;
    }

    public void setAuctionStartDatetime(long auctionStartDatetime) {
        this.auctionStartDatetime = auctionStartDatetime;
    }

    public long getAuctionEndDatetime() {
        return auctionEndDatetime;
    }

    public void setAuctionEndDatetime(long auctionEndDatetime) {
        this.auctionEndDatetime = auctionEndDatetime;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public BigDecimal getInvestGoalMoney() {
        return investGoalMoney;
    }

    public void setInvestGoalMoney(BigDecimal investGoalMoney) {
        this.investGoalMoney = investGoalMoney;
    }

    public BigDecimal getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(BigDecimal investsMoney) {
        this.investsMoney = investsMoney;
    }

    public long getCreationEndDatetime() {
        return creationEndDatetime;
    }

    public void setCreationEndDatetime(long creationEndDatetime) {
        this.creationEndDatetime = creationEndDatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNewCreationDate() {
        return newCreationDate;
    }

    public void setNewCreationDate(long newCreationDate) {
        this.newCreationDate = newCreationDate;
    }

    public int getAuctionNum() {
        return auctionNum;
    }

    public void setAuctionNum(int auctionNum) {
        this.auctionNum = auctionNum;
    }

    public int getNewBidingPrice() {
        return newBidingPrice;
    }

    public void setNewBidingPrice(int newBidingPrice) {
        this.newBidingPrice = newBidingPrice;
    }

    public Object getNewBiddingDate() {
        return newBiddingDate;
    }

    public void setNewBiddingDate(Object newBiddingDate) {
        this.newBiddingDate = newBiddingDate;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Object getFeedback() {
        return feedback;
    }

    public void setFeedback(Object feedback) {
        this.feedback = feedback;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public static class AuthorBean {
        private String id;
        private String username;
        private String name;
        private String pictureUrl;
        private Object cityId;
        private String status;
        private Object createDatetime;
        private String type;
        private int masterWorkNum;
        private int fansNum;

        public int getMasterWorkNum() {
            return masterWorkNum;
        }

        public void setMasterWorkNum(int masterWorkNum) {
            this.masterWorkNum = masterWorkNum;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        /**
         * id : icjxkedl0000b6i0
         * brief : 版画家，他使得业已消失数百年的明代印刷业老字号十竹斋重新恢复并焕发生机，成为杭州市文化产业传承创新的亮点。
         * title : 国家级传承人
         * favicon : http://tenant.efeiyi.com/background/蔡水况.jpg
         * birthday : 1968年
         * level : 1
         * content : null
         * presentAddress : 浙江
         * backgroundUrl : background/魏立中.jpg
         * provinceName : 浙江
         * theStatus : 1
         * logoUrl : logo/魏立中.jpg
         * masterSpeech : null
         * artCategory : null
         * titleCertificate : null
         * feedback : null
         * identityFront : null
         * identityBack : null
         */

        private MasterBean master;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public Object getCityId() {
            return cityId;
        }

        public void setCityId(Object cityId) {
            this.cityId = cityId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(Object createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public MasterBean getMaster() {
            return master;
        }

        public void setMaster(MasterBean master) {
            this.master = master;
        }

        public static class MasterBean {
            private String id;
            private String brief;
            private String title;
            private String favicon;
            private String birthday;
            private String level;
            private Object content;
            private String presentAddress;
            private String backgroundUrl;
            private String provinceName;
            private String theStatus;
            private String logoUrl;
            private Object masterSpeech;
            private Object artCategory;
            private Object titleCertificate;
            private Object feedback;
            private Object identityFront;
            private Object identityBack;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFavicon() {
                return favicon;
            }

            public void setFavicon(String favicon) {
                this.favicon = favicon;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }

            public String getPresentAddress() {
                return presentAddress;
            }

            public void setPresentAddress(String presentAddress) {
                this.presentAddress = presentAddress;
            }

            public String getBackgroundUrl() {
                return backgroundUrl;
            }

            public void setBackgroundUrl(String backgroundUrl) {
                this.backgroundUrl = backgroundUrl;
            }

            public String getProvinceName() {
                return provinceName;
            }

            public void setProvinceName(String provinceName) {
                this.provinceName = provinceName;
            }

            public String getTheStatus() {
                return theStatus;
            }

            public void setTheStatus(String theStatus) {
                this.theStatus = theStatus;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public Object getMasterSpeech() {
                return masterSpeech;
            }

            public void setMasterSpeech(Object masterSpeech) {
                this.masterSpeech = masterSpeech;
            }

            public Object getArtCategory() {
                return artCategory;
            }

            public void setArtCategory(Object artCategory) {
                this.artCategory = artCategory;
            }

            public Object getTitleCertificate() {
                return titleCertificate;
            }

            public void setTitleCertificate(Object titleCertificate) {
                this.titleCertificate = titleCertificate;
            }

            public Object getFeedback() {
                return feedback;
            }

            public void setFeedback(Object feedback) {
                this.feedback = feedback;
            }

            public Object getIdentityFront() {
                return identityFront;
            }

            public void setIdentityFront(Object identityFront) {
                this.identityFront = identityFront;
            }

            public Object getIdentityBack() {
                return identityBack;
            }

            public void setIdentityBack(Object identityBack) {
                this.identityBack = identityBack;
            }
        }
    }
//    private String id;
//    private String title;
//    private String brief;
//    private String description;
//    private String status;
//    private String type;
//    private String step;
//    private BigDecimal investGoalMoney;
//    private BigDecimal investsMoney;
//    private Long investStartDatetime;
//    private Long investEndDatetime;
//    private Long creationEndDatetime;
//    private Long newCreationEmdDatetime;
//    private Long auctionStartDatetime;
//    private BigDecimal newBidingPrice;
//    private Long bewBiddingDate;
//    private Long auctionEndDatetime;
//    private Long createDatetime;
//    private String picture_url;
//    private int investorsNum;
//    private User author;
//    private int auctionNum;
//
//    public int getAuctionNum() {
//        return auctionNum;
//    }
//
//    public void setAuctionNum(int auctionNum) {
//        this.auctionNum = auctionNum;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getBrief() {
//        return brief;
//    }
//
//    public void setBrief(String brief) {
//        this.brief = brief;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getStep() {
//        return step;
//    }
//
//    public void setStep(String step) {
//        this.step = step;
//    }
//
//    public BigDecimal getInvestGoalMoney() {
//        return investGoalMoney;
//    }
//
//    public void setInvestGoalMoney(BigDecimal investGoalMoney) {
//        this.investGoalMoney = investGoalMoney;
//    }
//
//    public Long getInvestStartDatetime() {
//        return investStartDatetime;
//    }
//
//    public void setInvestStartDatetime(Long investStartDatetime) {
//        this.investStartDatetime = investStartDatetime;
//    }
//
//    public Long getInvestEndDatetime() {
//        return investEndDatetime;
//    }
//
//    public void setInvestEndDatetime(Long investEndDatetime) {
//        this.investEndDatetime = investEndDatetime;
//    }
//
//    public Long getCreationEndDatetime() {
//        return creationEndDatetime;
//    }
//
//    public void setCreationEndDatetime(Long creationEndDatetime) {
//        this.creationEndDatetime = creationEndDatetime;
//    }
//
//    public Long getNewCreationEmdDatetime() {
//        return newCreationEmdDatetime;
//    }
//
//    public void setNewCreationEmdDatetime(Long newCreationEmdDatetime) {
//        this.newCreationEmdDatetime = newCreationEmdDatetime;
//    }
//
//    public Long getAuctionStartDatetime() {
//        return auctionStartDatetime;
//    }
//
//    public void setAuctionStartDatetime(Long auctionStartDatetime) {
//        this.auctionStartDatetime = auctionStartDatetime;
//    }
//
//    public BigDecimal getNewBidingPrice() {
//        return newBidingPrice;
//    }
//
//    public void setNewBidingPrice(BigDecimal newBidingPrice) {
//        this.newBidingPrice = newBidingPrice;
//    }
//
//    public Long getBewBiddingDate() {
//        return bewBiddingDate;
//    }
//
//    public void setBewBiddingDate(Long bewBiddingDate) {
//        this.bewBiddingDate = bewBiddingDate;
//    }
//
//    public Long getAuctionEndDatetime() {
//        return auctionEndDatetime;
//    }
//
//    public void setAuctionEndDatetime(Long auctionEndDatetime) {
//        this.auctionEndDatetime = auctionEndDatetime;
//    }
//
//    public Long getCreateDatetime() {
//        return createDatetime;
//    }
//
//    public void setCreateDatetime(Long createDatetime) {
//        this.createDatetime = createDatetime;
//    }
//
//    public String getPicture_url() {
//        return picture_url;
//    }
//
//    public void setPicture_url(String picture_url) {
//        this.picture_url = picture_url;
//    }
//
//    public int getInvestorsNum() {
//        return investorsNum;
//    }
//
//    public void setInvestorsNum(int investorsNum) {
//        this.investorsNum = investorsNum;
//    }
//
//    public User getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(User author) {
//        this.author = author;
//    }
//
//    public BigDecimal getInvestsMoney() {
//        return investsMoney;
//    }
//
//    public void setInvestsMoney(BigDecimal investsMoney) {
//        this.investsMoney = investsMoney;
//    }
}
