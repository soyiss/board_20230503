package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.repository.BoardRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 매개변수로 전달받은 boardDTO에는 아이디값이  아직 서버에 추가 전이니깐 값이없다(null)
    public void save(BoardDTO boardDTO) throws IOException {
        //파일 있음(1), 없음(0).
        //파일이 비었으면
        if(boardDTO.getBoardFile().isEmpty()){
            // 파일 없음
            System.out.println("파일없음");
            boardDTO.setFileAttached(0);
            boardRepository.save(boardDTO);
        }else{
            //파일 있음
            /*
            <흐름>
            * 1. 파일 첨부 여부 값 1로 세팅하고 DB에 제목 등 내용 board_table에 저장 처리
            * 2. 파일의 이름을 가져오고 DTO 값에 세팅
            * 3. 저장용 파일 이름을 만들고 DTO 필드값에 세팅
            * 4. 로컬에 파일 저장
            * 5. board_file_table에 저장 처리
            * */
            System.out.println("파일있음");
            boardDTO.setFileAttached(1);
            BoardDTO dto = boardRepository.save(boardDTO);
            // 원본 파일 이름 가져오기
            String originalFilename = boardDTO.getBoardFile().getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);
            // 저장용 이름 만들기
            // 첫번째 방법(이걸 자주씀)
            System.out.println(System.currentTimeMillis());
            // 두번쨰 방법(랜덤한 난수를 만들어주는 식/값이 너무 길어지는 단점이있음 )
            System.out.println(UUID.randomUUID().toString());
            String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
            System.out.println("storedFileName = " + storedFileName);
            //저장을 위한 BoardDileDTO세팅
            BoardFileDTO boardFileDTO = new BoardFileDTO();
            boardFileDTO.setOriginalFileName(originalFilename);
            boardFileDTO.setStoredFileName(storedFileName);
            boardFileDTO.setBoardId(dto.getId());
            //로컬에 파일 저장
            //저장할 경로 설정(저장할 폴더 + 저장할 이름)
            //어디(큰따옴표안에 쓴곳)에 어떤 이름(storedFileName)으로 저장할지 경로를 만들어줌
            // 큰따옴표 안에 젤 뒤에 백슬래시(\\) 두개 추가 필수 꼮꼮꼮꼬꼮ㄲ!!
            String savePath = "D:\\springframework_img\\" + storedFileName;
            //저장처리
            //파일을 보드디티오에 보드파일 필드에 저장함
            boardDTO.getBoardFile().transferTo(new File(savePath));
            boardRepository.saveFile(boardFileDTO);
        }

    }

    public List<BoardDTO> findAll() {
        return boardRepository.findAll();
    }
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        return boardRepository.fidById(id);
    }

    public void update(BoardDTO boardDTO) {
        boardRepository.update(boardDTO);
    }

    public void delete(Long id) {
        boardRepository.delete(id);
    }

    public BoardFileDTO findFile(Long id) {

        return boardRepository.findFile(id);

    }
}
