package com.icia.board.repository;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.service.BoardService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Repository
public class BoardRepository {

    @Autowired
    private SqlSessionTemplate sql;


    public BoardDTO save(BoardDTO boardDTO) {
        //파일이 추가될때 어떤 게시글에 추가가되는지 id값을 알아야된다.
        //insert 하기 전에 아이디값은 널이다
        System.out.println("insert 전 boardDTO = " + boardDTO);
        sql.insert("Board.save",boardDTO);
        //insert 하고 나선 id값이 추가됨
        //매퍼에서 useGeneratedKeys="true" keyProperty="id"속성을 적용해서 아이디 값을 가져와서 id값이 널이 안뜸
        //그래서 방금 저장한 게시글의 글번호를 알수있게 됨
        //아이디값을 서비스로 넘겨줘야됨
        System.out.println("insert 후 boardDTO = " + boardDTO);
        //매개변수로 전달받은 boardDTO랑 리턴 받은 boardDTO는 아이디가 다름(매개변수는 아이디값없음null)
        return boardDTO;

    }

    public List<BoardDTO> findAll() {
        return sql.selectList("Board.findAll");
    }
    public void updateHits(Long id) {
        sql.update("Board.updateHits", id);
    }

    public BoardDTO fidById(Long id) {
        return sql.selectOne("Board.findById", id);
    }

    public void update(BoardDTO boardDTO) {
        sql.update("Board.update", boardDTO);
    }

    public void delete(Long id) {
        sql.delete("Board.delete",id);
    }

    public void saveFile(BoardFileDTO boardFileDTO) {

        sql.insert("Board.saveFile",boardFileDTO);
    }

    public List<BoardFileDTO> findFile(Long boardId) {
        //하나의 게시글에 첨부파일이 여러개니까 LIst로 받아줌
        return sql.selectList("Board.findFile", boardId);
    }
}
