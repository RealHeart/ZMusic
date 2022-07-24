package me.zhenxin.zmusic.bridge.entity;

/**
 * 消息信息
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2022/7/24 12:01
 */
public class MusicInfo {
    /**
     * 歌名
     */
    private String name;
    /**
     * 歌手
     */
    private String singer;
    /**
     * 歌词
     */
    private String lyric;
    /**
     * 当前时间
     */
    private Long currentTime;
    /**
     * 最大时间
     */
    private Long maxTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }
}
