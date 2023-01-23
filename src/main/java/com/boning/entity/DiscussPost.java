package com.boning.entity;

import java.util.Date;

/**
 * 帖子
 */
//es 7.0需注意地方 shards ,replicas 在document弃用，改用setting注入
//6个分片 3个备份
//@Setting(shards = 6, replicas = 3)
//@Document(indexName = "discusspost")
//加上注解和es建立联系

/**
 * @Author: Ctc
 * @Date: 2022/3/25 13:02
 */
public class DiscussPost {
    //    @Id
    private int id;

    //    @Field(type = FieldType.Integer)
    private int userId;

    //title:互联网校招
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    //    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    //    @Field(type = FieldType.Integer)
    private int type;

    //    @Field(type = FieldType.Integer)
    private int status;

    //    @Field(type = FieldType.Date)
    private Date createTime;

    //    @Field(type = FieldType.Integer)
    private int commentCount;

    //    @Field(type = FieldType.Double)/
    private double score;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
