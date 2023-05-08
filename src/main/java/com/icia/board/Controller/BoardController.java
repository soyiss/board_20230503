package com.icia.board.Controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.dto.CommentDTO;
import com.icia.board.service.BoardService;
import com.icia.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller

//공통된 주소값을 밖으로 뺄 수있다
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    //    @GetMapping("/board/save")
    @GetMapping("/save")
    public String saveForm() {
        return "boardPages/boardSave";
    }


    //    @PostMapping("/board/save")
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO =" + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    // @GetMapping("/board/")
    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "boardPages/boardList";
    }


    // /board?id=1
    //주소가 /board인데 위에 묶어줘서 안써도됨
    @GetMapping
    public String findById(@RequestParam("id") Long id, Model model) {
        //조회수를 1씩 증가시키는 메소드이다
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        if(boardDTO.getFileAttached() == 1){
            // 파일이 있는 게시글을 선택하면
            List<BoardFileDTO> boardFileDTO = boardService.findFile(id);
            model.addAttribute("boardFileList", boardFileDTO);
            System.out.println("boardFileDTO = " + boardFileDTO);
        }
        //댓글을 가져와야됨
        //매개변수의 id는 게시글의 id이다
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        if(commentDTOList.size() == 0){
            //댓글이 없으면 list에 null적용
            model.addAttribute("commentList", null);
        }else{
            //댓글이 있으면 서버에서 가져온 commentDTOList를 넘겨준다
            model.addAttribute("commentList", commentDTOList);
        }
            return "boardPages/boardDetail";


    }

    @GetMapping("/update")
    public String updateForm(@RequestParam("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardPages/boardUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        boardService.update(boardDTO);

        //수정된 내용의 id를 findById메소드에 매개변수로 보낸후 dto변수에 담아옴
        BoardDTO dto = boardService.findById(boardDTO.getId());
        model.addAttribute("board", dto);

        //resirect로 요청하면 수정한건데 조회수가 올라가니까 밑에 리턴처럼 해줘야됌
//        return "redirect:/board?id="+boardDTO.getId();

        //수정한 객체를 다시 모델에 담아가서 detail.jsp에 뿌려준다
        return "boardPages/boardDetail";
    }

    @GetMapping("/delete-check")
    public String deleteCheck(@RequestParam("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardPages/deleteCheck";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }
}
