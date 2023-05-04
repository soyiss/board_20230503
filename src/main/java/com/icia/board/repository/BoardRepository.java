package com.icia.board.repository;

import com.icia.board.dto.BoardDTO;
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


    public void save(BoardDTO boardDTO) {

        sql.insert("Board.save",boardDTO);

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
}
