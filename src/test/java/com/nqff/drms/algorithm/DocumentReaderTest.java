package com.nqff.drms.algorithm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocumentReaderTest {
    @Test
    void readDocx(){
        System.out.println(DocumentReader.readDocx("D:\\桌面\\作业\\批判性思维\\2023软件学院批判性思维考试评分说明.docx"));
    }

    @Test
    void readPdf(){
        System.out.println(DocumentReader.readPdf("D:\\桌面\\作业\\软工课设\\软件工程课程设计-实验指导书2023定稿.pdf"));
    }
}