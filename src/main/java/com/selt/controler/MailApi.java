package com.selt.controler;

import com.selt.service.CounterService;
import com.selt.service.MailService;
import com.selt.service.PrinterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;

@RequiredArgsConstructor
@RestController
public class MailApi {
    private final MailService mailService;
    private final CounterService counterService;
    private final PrinterService printerService;

    @GetMapping("/sendMail")
    public String sendMail() throws MessagingException {counterService.onlineList();
     printerService.reload();


            return "wysłano";

    }

//    @RequestMapping(value="/sendMail", method= RequestMethod.GET)
//    public ResponseEntity<byte[]> getPDF1() {
//
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.valueOf("src/main/resources/Protocol"));
//        String filename = "2023-06-07-11403";
//
//        headers.add("content-disposition", "inline;filename=" + filename);
//
//        headers.setContentDispositionFormData(filename, filename);
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(headers, HttpStatus.OK);
//        return response;
//    }
//
//    @GetMapping(value = "/sendMail")
//    public ResponseEntity<InputStreamResource> getTermsConditions() throws FileNotFoundException {
//
//        String filePath = "src/main/resources/Protocol/";
//        String fileName = "2023-06-07-11403"+".pdf";
//        File file = new File(filePath+fileName);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("content-disposition", "inline;filename=" +fileName);
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/pdf"))
//                .body(resource);
//    }
}
