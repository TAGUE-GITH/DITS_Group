package com.dits.dits_group.common.storage;

import com.dits.dits_group.document.service.DocumentFileStorageService;
import com.dits.dits_group.recruitment.service.JobApplicationFileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Documents des clients et CV des candidats : recopies depuis la base si le disque a ete efface. */
@SpringBootTest
@Transactional
class DocumentStorageTest {

    @Autowired
    private DocumentFileStorageService documents;

    @Autowired
    private JobApplicationFileStorageService cvs;

    @Test
    void aClientDocumentIsRestoredWhenItIsDownloaded() throws Exception {
        byte[] pdf = "%PDF-1.7 contrat".getBytes();
        String name = documents.store(new MockMultipartFile("file", "contrat.pdf", "application/pdf", pdf));
        Path onDisk = documents.getFilePath(name);
        Files.delete(onDisk);

        assertThat(Files.readAllBytes(documents.getFilePath(name))).isEqualTo(pdf);

        documents.delete(name);
        assertThat(Files.exists(onDisk)).isFalse();
    }

    @Test
    void aCandidateCvIsRestoredWhenItIsDownloaded() throws Exception {
        byte[] pdf = "%PDF-1.7 cv".getBytes();
        String name = cvs.store(new MockMultipartFile("cv", "cv.pdf", "application/pdf", pdf));
        Files.delete(cvs.getFilePath(name));

        assertThat(Files.readAllBytes(cvs.getFilePath(name))).isEqualTo(pdf);
    }
}
