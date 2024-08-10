//package org.example.anibuddy.store;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//public class StoreController {
//	 private final StoreService storeService;
//
//	    @GetMapping("/all")
//	    public List<StoreEntity> getStoreAll(){
//	        return storeService.findAll();
//	    }
//
//	    @PostMapping("/create")
//	    public ResponseEntity<?> createStore(@RequestBody StoreCreateDto storeCreateDto){
//	        ResponseEntity  response = storeService.createStore(storeCreateDto);
//	        return response;
//	    }
//
//	    @GetMapping("/main")
//	    public List<MainReviewSimpleResponseDto> getMainStore(MainReviewSimpleRequestDto resqeust){
//	        System.out.println(resqeust.getMapx());
//	        List<MainReviewSimpleResponseDto> storeEntityList = storeService.getMainStore(resqeust.getMapx(), resqeust.getMapy(), resqeust.getCategory());
//	        return storeEntityList;
//	    }
//
//	    @PostMapping("/create/all")
//	    public String createStoreAll(@RequestBody List<StoreCreateDto> storeCreateDtoList){
//	        storeService.createStoreAll(storeCreateDtoList);
//	        return "success";
//	    }
//
//	    @PostMapping("/category")
//	    public String category(@RequestBody List<StoreCreateDto> storeCreateDtoList){
//	        storeService.setCategory(storeCreateDtoList);
//	        return "success";
//	    }
//
//	    @GetMapping("/search/location")
//	    public List<StoreSearchLocationCategoryResponse> serachLocationCategory(StoreSearchLocationCategoryRequestDto reqeust){
//	        return storeService.serachLocationCategory(reqeust.getDistrict(),reqeust.getCategory(),reqeust.getMapx(), reqeust.getMapy(), reqeust.getName());
//	    }
//	
//}
//
//
