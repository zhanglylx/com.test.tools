package operation_plug.foundation_platform.acquisition_audio_management;

/**
 * 采集音频作品对象
 */
public class Production {
    private long cpId;
    private String title;//作品名称
    private String authorName;//作者
    private String lecturer;//主播
    private String introduction;//作品简介
    private String shortIntroduction;//短简介
    private String lecturerIntroduction;//主播简介
    private String keyword;//作品标签
    private String cpAudioId;//音频ID
    private byte audioStatus;//连载状态  1.连载  3.完结
    private byte categoryId;//2:都市传说 3:悬疑恐怖 4:总裁豪门 5玄幻武侠 6:古代言情 7:都市言情 8:穿越架空 9:官商人文

    public Production() {
    }

    public Production(long cpId, String title, String authorName, String lecturer, String introduction, String shortIntroduction, String lecturerIntroduction, String keyword, String cpAudioId, byte audioStatus, byte categoryId) {
        this.cpId = cpId;
        this.title = title;
        this.authorName = authorName;
        this.lecturer = lecturer;
        this.introduction = introduction;
        this.shortIntroduction = shortIntroduction;
        this.lecturerIntroduction = lecturerIntroduction;
        this.keyword = keyword;
        this.cpAudioId = cpAudioId;
        this.audioStatus = audioStatus;
        this.categoryId = categoryId;
    }


    public long getCpId() {
        return cpId;
    }

    public void setCpId(long cpId) {
        this.cpId = cpId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getShortIntroduction() {
        return shortIntroduction;
    }

    public void setShortIntroduction(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }

    public String getLecturerIntroduction() {
        return lecturerIntroduction;
    }

    public void setLecturerIntroduction(String lecturerIntroduction) {
        this.lecturerIntroduction = lecturerIntroduction;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCpAudioId() {
        return cpAudioId;
    }

    public void setCpAudioId(String cpAudioId) {
        this.cpAudioId = cpAudioId;
    }

    public byte getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(byte audioStatus) {
        this.audioStatus = audioStatus;
    }

    public byte getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(byte categoryId) {
        this.categoryId = categoryId;
    }


}
