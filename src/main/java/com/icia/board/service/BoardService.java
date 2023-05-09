package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.BoardFileDTO;
import com.icia.board.dto.PageDTO;
import com.icia.board.repository.BoardRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 매개변수로 전달받은 boardDTO에는 아이디값이  아직 서버에 추가 전이니깐 값이없다(null)
    public void save(BoardDTO boardDTO) throws IOException {
        //파일 있음(1), 없음(0).
        //파일이 비었으면
        //BoardFile은 리스트 객체이니까 get(0)은 0번 인덱스의 파일을 가져와서 없으면 isEmpty
        if(boardDTO.getBoardFile().get(0).isEmpty()){
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
            //dto는 사용자가 입력한 인풋값을 담음(아이디가값이 담겨있음)
            BoardDTO dto = boardRepository.save(boardDTO);
            //파일이 여러개니까 반복문을 돌려야됌
            //여러개의 파일을 하나씩 처리해줘야됌
            for(MultipartFile boardFile: boardDTO.getBoardFile()) {

                // 원본 파일 이름 가져오기
                String originalFilename = boardFile.getOriginalFilename();
                System.out.println("originalFilename = " + originalFilename);

                // 저장용 이름 만들기

                // 첫번째 방법(이걸 자주씀)
                System.out.println(System.currentTimeMillis());

                // 두번쨰 방법(랜덤한 난수를 만들어주는 식/값이 너무 길어지는 단점이있음 )
                System.out.println(UUID.randomUUID().toString());
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                System.out.println("storedFileName = " + storedFileName);

                //저장을 위한 BoardFileDTO세팅
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
                // 리스트에 담겨있는데 for문으로 접근하고있으므로 각각 첨부파일로 접근함
                boardFile.transferTo(new File(savePath));

                //boardFileDTO여기에는 인풋에서 입력받아온 원본파일이름이랑, 저장파일이름, 아이디값이 들어있음
                boardRepository.saveFile(boardFileDTO);
            }
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

    public List<BoardFileDTO> findFile(Long id) {

        return boardRepository.findFile(id);

    }

    public List<BoardDTO> pagingList(int page) {

        int pageLimit = 3; // 한페이지에 보여줄 글 목록 갯수
        int pagingStart = (page-1) * pageLimit; //사용자가 보고싶은 페이지의 게시글 시작 번호(?)
        Map<String, Integer> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("Limit", pageLimit);
        List<BoardDTO> boardDTOList = boardRepository.pagingList(pagingParams);
        return boardDTOList;


    }

    public PageDTO pagingParam(int page) {

        int pageLimit = 3; //한 페이지에 보여줄 글 갯수
        int blockLimit = 3; //하단에 보여줄 페이지 번호 갯수
        // 한페이지에 글을 3개씩 보여주고 밑에 나오는 숫자는 123/456/789로 보여줄거임
        // 사용자가 2페리지 보여주세요 하면 하단의 보이는 숫자는 123
        // 9페이를 보여달라고 하면 하단의 보이는 숫자는 789
        // 하단의 숫자는 게시글이 있는 만큼만 보여ㅜㅈ면된다(게시글이 13개있는데 3개씩 보여주면 6페이지는 없어도됌)

        // 전체 글 갯수 조회
        int boardCount = boardRepository.boardCount();

        // 전체 페이지 갯수 계산
        //전체 글게수를 실수로 바꿔서 3의로 나눈 뒤 올림처리를 한 후 정수로 형변환을 해서 maxPage에 담는다
        int maxPage = (int)(Math.ceil((double)boardCount / pageLimit)); // 정수/정수 = 정수(소수는 버림)-->올림처리를 해서 소수점도 가져가야됨

        //시작 페이지 값 계산(1,4,7,10~~`~`)
        int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지 값 계산(3,6,9,12~~)
        int endPage = startPage + blockLimit -1;

        // 전체 페이지 갯수가 계산한 endPage보다 작을 때는 endPAge값을 maxPage 값과 같게 세팅
        // 맥스랑 앤드를 비교해서 필요없으면 맥스를 앤드로
        //13게시글에서 엔드를 6으로 했는데 맥스는 5일때 6은 필요없으니까 맥스값을 넣어서 앤드를 5로 해줌
        if(endPage > maxPage){
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setEndPage(endPage);
        pageDTO.setStartPage(startPage);
        return pageDTO;

    }


    //사용자가 요청한 검색어에 해당하는 결과에서 해당 페이지에 대한 데이터를 가져옴
    public List<BoardDTO> searchList(int page,String type, String q) {
        int pageLimit = 3; // 한페이지에 보여줄 글 갯수
        int pagingStart = (page-1) * pageLimit;
        // 검색어는 string  나머지는 int를 다 map에서 받아야되서 셋다 뭉쳐서 object형으로 씀
        Map<String, Object> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("limit", pageLimit);
        pagingParams.put("q", q);
        pagingParams.put("type", type);
        List<BoardDTO> boardDTOList = boardRepository.searchList(pagingParams);
        return boardDTOList;
    }

    public PageDTO pagingSearchParam(int page,String type, String q) {
        int pageLimit = 3; // 한페이지에 보여줄 글 갯수
        int blockLimit = 3; // 하단에 보여줄 페이지 번호 갯수
        Map<String, Object> pagingParams = new HashMap<>();
        pagingParams.put("q", q);
        pagingParams.put("type", type);
        // 전체 글 갯수 조회
        // 검색어가 포함된 게시글 갯수를 알아야됌
        int boardCount = boardRepository.boardSearchCount(pagingParams);
        // 전체 페이지 갯수 계산
        int maxPage = (int) (Math.ceil((double)boardCount / pageLimit));
        // 시작 페이지 값 계산(1, 4, 7, 10 ~~)
        int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 마지막 페이지 값 계산(3, 6, 9, 12 ~~)
        int endPage = startPage + blockLimit - 1;
        // 전체 페이지 갯수가 계산한 endPage 보다 작을 때는 endPage 값을 maxPage 값과 같게 세팅
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setEndPage(endPage);
        pageDTO.setStartPage(startPage);
        return pageDTO;
    }




}



