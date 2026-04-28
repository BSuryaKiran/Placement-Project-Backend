package com.placement.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table(name = "file_uploads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileType; // resume, profile_picture, etc.

    @Column(nullable = false)
    private Long fileSize; // in bytes

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private Date uploadedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
