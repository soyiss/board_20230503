package com.icia.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString

public class BoardDTO {

    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private Timestamp boardCreatedDate;
    private int boardHits;
    private int fileAttached;


//   MultipartFile은 하나의 사진만 넘어오게 하는거다

//    private MultipartFile boardFile;

    //여러개로 받으려면 List를 해주면 된다
    private List<MultipartFile> boardFile;








}
