package org.example.anibuddy;

import jakarta.transaction.Transactional;
import org.example.anibuddy.store.entity.StoreEntity;
//import org.example.anibuddy.store.entity.StoreSummary;
import org.example.anibuddy.store.repository.StoreRepository;
//import org.example.anibuddy.store.repository.StoreSummaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.anibuddy.pet.PetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnibuddyApplicationTests {
	@Test
	public void testPetDTOConstructor() {
		for(int i=0 ; i <100000; i ++) {
			System.out.println(i);
		}
		
	}
}


//    @Test
//    public void testPetDTOConstructor() {
//        PetDTO dto = new PetDTO("SampleName", "SampleKind", "SampleGender", 5, 1);
//        assertEquals("SampleName", dto.getPetName());
//        assertEquals("SampleKind", dto.getPetKind());
//        assertEquals("SampleGender", dto.getPetGender());
//        assertEquals(5, dto.getPetAge());
//        assertEquals(1, dto.getPetId());
//    }
