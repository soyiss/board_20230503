package com.icia.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class CommentDTO {

    private Long id; //댓글 번호
    private String commentWriter; //댓글 작성자
    private String commentContents; //댓글 내용
    private Long boardId; //어떤 게시글의 댓글인지
    private Timestamp commentCreateDate; // 댓글 작성 시간


}
