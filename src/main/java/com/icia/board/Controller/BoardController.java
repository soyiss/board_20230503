package com.icia.board.Controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.dto.CommentDTO;
import com.icia.board.dto.PageDTO;
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

import javax.servlet.http.HttpSession;
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

    // 페이징 처리를 위한 메소드
    // 사용자가 필요한 페이지가 몊인지 알아야디니까 파라미터로 page를 받아야된다
    //required = false를 써서 page는 필수 옵션이 아니여도된다고 하고 page 파라미터가 없으면 기본겂으로 1로 지정해줌
    //page에 특정값이 있으면 파라미터로 넘어오고 없으면 1로 세팅하겠다는 뜻

    @GetMapping("/paging")
    public String paging(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "q", required = false, defaultValue = "") String q,
                         @RequestParam(value = "type",required = false, defaultValue = "boardTitle") String type,
                         Model model) {
        System.out.println("page = " + page + ", q = " + q);
        List<BoardDTO> boardDTOList = null;
        PageDTO pageDTO = null;

        // 검색어 파라미터 q값이 없으면 일반 페이징 처리를 해라
        if (q.equals("")) {
            // 사용자가 요청한 페이지에 해당하는 글 목록 데이터
            // 사용자가 3페이지를 보고싶다 하면 3페이지에 해당하는 내용 목록들
            boardDTOList = boardService.pagingList(page);
            // 하단에 보여줄 페이지 번호 목록 데이터
            pageDTO = boardService.pagingParam(page);
        } else {

        // 검색어가 있으면 검색어가 포함된 페이징 처리를 해라
            boardDTOList = boardService.searchList(page,type,q);
            pageDTO = boardService.pagingSearchParam(page,type,q);
        }
        // 페이지에 들어가는 글 목록들
        model.addAttribute("boardList", boardDTOList);
        // 하단에 보여줄 페이지 목록들
        model.addAttribute("paging", pageDTO);
        model.addAttribute("q", q);
        model.addAttribute("type",type);

        return "boardPages/boardPaging";
    }


    // /board?id=1
    //주소가 /board인데 위에 묶어줘서 안써도됨
    @GetMapping
    public String findById(@RequestParam("id") Long id, Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "q", required = false, defaultValue = "") String q,
                           @RequestParam(value = "type",required = false, defaultValue = "boardTitle") String type) {
        //조회수를 1씩 증가시키는 메소드이다
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page",page);
        model.addAttribute("q",q);
        model.addAttribute("type", type);
        if (boardDTO.getFileAttached() == 1) {
            // 파일이 있는 게시글을 선택하면
            List<BoardFileDTO> boardFileDTO = boardService.findFile(id);
            model.addAttribute("boardFileList", boardFileDTO);
            System.out.println("boardFileDTO = " + boardFileDTO);
        }
        //댓글을 가져와야됨
        //매개변수의 id는 게시글의 id이다
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        if (commentDTOList.size() == 0) {
            //댓글이 없으면 list에 null적용
            model.addAttribute("commentList", null);
        } else {
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

//    @GetMapping("/search")
////    q=검색어,,,검색결과기준으로 몇페이지를 요청했는지 알아야됌
//
//    public String search(@RequestParam("q") String q, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//                         Model model) {
//        //검색 리스트를 가져오는 메소드
//        List<BoardDTO> boardDTOList = boardService.searchList(q, page);
//        PageDTO pageDTO = boardService.pagingSearchParam(page, q);
//        model.addAttribute("boardList", boardDTOList);
//        model.addAttribute("paging", pageDTO);
//        model.addAttribute("q", q);
//        return "boardPages/boardPaging";
//    }
}
