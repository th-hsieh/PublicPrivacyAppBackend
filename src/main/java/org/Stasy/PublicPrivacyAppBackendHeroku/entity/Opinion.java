package org.Stasy.PublicPrivacyAppBackendHeroku.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table(name="opinion")
public class Opinion {
    @Id
    @Column(name="opinion_id",nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    //if not specified "@Column", the table will automatically generate one with the field name.

    @Column(name="collaborator_name",nullable = false)
    private String collaboratorName;

    @Column(nullable=false)
    private String title;
    @Column(nullable=false)
    private String body;
    @Column(name="category",nullable = false)
    private String category;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name="created_at",nullable = false,updatable = false)
    @CreationTimestamp
    private Date createdAt;

    public Opinion(Integer id, String collaboratorName, String title, String body, String category, Date updatedAt, Date createdAt) {
        this.id = id;
        this.collaboratorName = collaboratorName;
        this.title = title;
        this.body = body;
        this.category = category;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Opinion(){};
//    @JsonCreator
//    public Opinion(@JsonProperty("id") Integer id, @JsonProperty("collaboratorName") String collaboratorName,
//                 @JsonProperty("title") String title, @JsonProperty("body") String body,@JsonProperty("category") String category,@JsonProperty("updatedAt") Date updatedAt,@JsonProperty("createdAt")Date createdAt ) {
//        this.id = id;
//        this.collaboratorName = collaboratorName;
//        this.title = title;
//        this.body = body;
//        this.category = category;
//        this.updatedAt=updatedAt;
//        this.createdAt=createdAt;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCollaboratorName(String collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public String getCollaboratorName() {
        return collaboratorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

//    public void setBody(String body) {
//        this.body = body;
//    }

    public void setBody(String body) {
        if (body != null && !body.isEmpty()) {
            String[] words = body.trim().split("\\s+");
            if (words.length > 500) {
                StringBuilder truncatedBody = new StringBuilder();
                for (int i = 0; i <= 500; i++) {
                    truncatedBody.append(words[i]).append(" ");
                }
                body = truncatedBody.toString().trim();
            }
        }
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Opinion{" +
                "id=" + id +
                ", collaboratorName='" + collaboratorName + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", category='" + category + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }

    public String findCollaboratorNameById(Integer id) {
        return collaboratorName;
    }
}




