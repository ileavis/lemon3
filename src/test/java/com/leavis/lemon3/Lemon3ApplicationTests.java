package com.leavis.lemon3;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jd.platform.async.executor.Async;
import com.jd.platform.async.wrapper.WorkerWrapper;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class Lemon3ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUserPage() throws Exception {
        this.mockMvc.perform(get("/user/page"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SYS00000")));
    }

    @Test
    public void testWeather() throws Exception {
        this.mockMvc.perform(get("/amap/weather?cityCode=110000"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("SYS00000")));
    }


    @Test
    void test() throws ExecutionException, InterruptedException {
        DeWorker w = new DeWorker();
        DeWorker1 w1 = new DeWorker1();
        DeWorker2 w2 = new DeWorker2();

        WorkerWrapper<User, String> workerWrapper2 = new WorkerWrapper.Builder<User, String>()
                .worker(w2)
                .callback(w2)
                .id("third")
                .build();

        WorkerWrapper<String, User> workerWrapper1 = new WorkerWrapper.Builder<String, User>()
                .worker(w1)
                .callback(w1)
                .id("second")
                .next(workerWrapper2)
                .build();

        WorkerWrapper<String, User> workerWrapper = new WorkerWrapper.Builder<String, User>()
                .worker(w)
                .param("0")
                .id("first")
                .next(workerWrapper1)
                .callback(w)
                .build();

        //V1.3后，不用给wrapper setParam了，直接在worker的action里自行根据id获取即可

        Async.beginWork(1500, workerWrapper);

        System.out.println(workerWrapper2.getWorkResult());
        Async.shutDown();
    }

}
