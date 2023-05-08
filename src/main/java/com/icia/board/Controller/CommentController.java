package com.icia.board.Controller;

import com.icia.board.dto.CommentDTO;
import com.icia.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    public CommentService commentService;


//    @PostMapping("/comment/save")
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO){

        //프론트(boardDetail.jsp)에서 컨트롤러(CommentController)로 데이터가 잘 오는지 확인하는 과정(sout찍어보기)
        //sout에는 작성자, 댓글내용,보드아이디값이 콘솔에 잘 찍힘(서버에는 아직 저장되지 않아서 id,commentCreateDate는 null이 찍힌다
        //요류 없이 잘 넘어왔으면 이제 DB로 넘어가자!
        System.out.println("commentDTO = " + commentDTO);

        //서버로 가자~!
        commentService.save(commentDTO);

        //댓글목록 출력할 때 필요한 부분
        //해당 게시글에 작성된 댓글만 가져와야된다 (DB에서 그 게시글에 작성된 댓글만 가져와야되니까 매개변수가 필요하다)
        List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
}
