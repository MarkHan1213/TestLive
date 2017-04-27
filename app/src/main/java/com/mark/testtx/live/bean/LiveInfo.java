package com.mark.testtx.live.bean;

/**
 * 直播间属性
 * Created by Mark.Han on 2017/4/26.
 */
public class LiveInfo {

/*    userid	string	用户id
    groupid	string	群组id
    timestamp	int	开始推流的时间戳
    type	int	0:直播 1:录播
    viewercount	int	在线数量
    likecount	int	点赞数量
    title	string	直播标题
    playurl	string	播放地址
    fileid	string	点播的文件id

    status	string	0:离线 1:在线
    hls_play_url	string	hls播放地址
    userinfo	object	用户信息，同RequstLVBAddr中的定义*/

    public String userid;
    public String groupid;
    public int timestamp;
    public int type;
    public int viewercount;
    public int likecount;
    public String title;
    public String playurl;
    public String fileid;

    private String status;//0:离线 1:在线
    private String hls_play_url;// hls播放地址

    //TCLiveUserInfo
    public LiveUserInfo userinfo;

}
