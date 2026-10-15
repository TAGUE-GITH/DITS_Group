package com.dits.dits_group.common.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Les fichiers envoyes survivent a l'effacement du disque (redeploiement chez l'hebergeur gratuit). */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FileStorageTest {

    @Autowired
    private FileStorageService storage;

    @Autowired
    private StoredFileRepository storedFiles;

    @Autowired
    private StoredFileRestorer restorer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anImageIsKeptInTheDatabaseAndRestoredAfterTheDiskIsWiped() throws Exception {
        byte[] png = { (byte) 0x89, 'P', 'N', 'G', 1, 2, 3 };
        String url = storage.store(new MockMultipartFile("image", "Bureau.PNG", "image/png", png), "services");

        assertThat(url).startsWith("/uploads/services/").endsWith(".png");
        String path = url.substring("/uploads/".length());
        assertThat(storedFiles.findById(path)).isPresent();
        mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(content().bytes(png));

        Files.delete(storage.resolve(path));
        restorer.run(null);

        assertThat(Files.readAllBytes(storage.resolve(path))).isEqualTo(png);
        mockMvc.perform(get(url)).andExpect(status().isOk());

        storage.delete(url);
        assertThat(storedFiles.findById(path)).isEmpty();
        assertThat(Files.exists(storage.resolve(path))).isFalse();
    }

    @Test
    void pathsCannotLeaveTheUploadFolder() {
        assertThatThrownBy(() -> storage.resolve("../application.properties")).isInstanceOf(IllegalArgumentException.class);
    }
}
