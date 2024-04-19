package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.StockInDTO;
import com.keysoft.ecommerce.service.StockInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stock")
@Slf4j
public class StockInController {
    @Autowired
    private StockInService stockInService;
    @PostMapping("/list")
    public ResponseEntity<?> getAllStockIns(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all stock in");
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setPage(page);
        stockInDTO.setSize(size);
        Page<StockInDTO> results = stockInService.getAllStockIns(stockInDTO);
        if(results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập kho nào!");
        }else {
            return ResponseEntity.ok(results);
        }

    }

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody StockInDTO stockInDTO) throws IllegalAccessException {
        log.info("controller: save stock in");
        if(stockInService.save(stockInDTO)) {
            return ResponseEntity.ok("Lưu thành công");
        }
        return ResponseEntity.badRequest().body("Lỗi khi lưu");
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") String id) {
        log.info("controller: get detail stock in");
        try {
            return ResponseEntity.ok(stockInService.get(id));
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
