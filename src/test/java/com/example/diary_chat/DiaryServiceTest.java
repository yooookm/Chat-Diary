package com.example.diary_chat;

// @ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

//    @Mock
//    private DiaryRepository diaryRepository;
//
//    @InjectMocks
//    private DiaryService diaryService;
//
//    private Diary diary;
//    private DiaryRequest diaryRequest;
//
//    @BeforeEach
//    public void setUp() {
//        diaryRequest = new DiaryRequest("Title", "Content", LocalDate.now(), "HAPPY");
//        diary = new Diary(1L, "Title", "Content", LocalDate.now(), "HAPPY");
//
//    }
//
//    @Test
//    public void testGetAllDiariesByUserId() {
//        when(diaryRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(diary));
//        List<Diary> results = diaryService.getAllDiariesByUserId(1L);
//        assertFalse(results.isEmpty());
//        assertEquals(1, results.size());
//        assertEquals(diary, results.get(0));
//    }
//
//    @Test
//    public void testGetDiaryById() {
//        when(diaryRepository.findById(anyLong())).thenReturn(Optional.of(diary));
//
//        Diary result = diaryService.getDiaryById(1L);
//        assertNotNull(result);
//        assertEquals("Title", result.getTitle());
//    }
//
//    @Test
//    public void testGetDiaryByIdNotFound() {
//        when(diaryRepository.findById(anyLong())).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> {
//            diaryService.getDiaryById(999L);
//        });
//    }
//
//    @Test
//    public void testAddDiary() {
//        when(diaryRepository.save(any(Diary.class))).thenReturn(diary);
//        Diary result = diaryService.addDiary(diaryRequest, 1L);
//        assertNotNull(result);
//        assertEquals("Title", result.getTitle());
//        assertEquals("Content", result.getContent());
//    }
//
//    @Test
//    public void testUpdateDiary() {
//        when(diaryRepository.findById(anyLong())).thenReturn(Optional.of(diary));
//        diaryService.updateDiary(diaryRequest, 1L, 1L);
//        verify(diaryRepository).save(diary);
//        assertEquals("Title", diary.getTitle());
//        assertEquals("Content", diary.getContent());
//    }
//
//    @Test
//    public void testDeleteDiary() {
//        doNothing().when(diaryRepository).deleteById(diary.getId());
//        diaryService.deleteDiary(diary.getId(), 1L);
//        verify(diaryRepository).deleteById(diary.getId());
//    }
}