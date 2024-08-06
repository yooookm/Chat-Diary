package com.example.diary_chat.domain;

import com.example.diary_chat.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET status = false WHERE id = ?")
//@SQLRestriction("status = true")
@Where(clause = "status = true")
public class Member extends BaseEntity {
    @Column(length = 16, nullable = false, unique = true)
    private String nickname;

    @Column(length = 64, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;
}

