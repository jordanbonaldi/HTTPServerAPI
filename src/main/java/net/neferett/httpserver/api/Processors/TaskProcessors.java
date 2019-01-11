package net.neferett.httpserver.api.Processors;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Data
public class TaskProcessors {

    @Data
    public class TaskProcesses implements Callable<TaskProcess> {

        private final TaskProcess taskProcess;

        @Override
        @SneakyThrows
        public TaskProcess call() {
            this.taskProcess.run();

            return this.taskProcess;
        }
    }

    private List<TaskProcesses> taskProcessesList;

    private ThreadPoolExecutor executorService;

    public TaskProcessors(int threads, TaskProcess ... taskProcesses) {
        this.taskProcessesList = Arrays.stream(taskProcesses).map(TaskProcesses::new).collect(Collectors.toList());

        this.executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public void add(TaskProcess process) {
        this.taskProcessesList.add(new TaskProcesses(process));
    }

    @SneakyThrows
    public void invokeAll() {
        this.executorService.invokeAll(this.taskProcessesList);
    }

    public void shutDown() {
        this.executorService.shutdown();
    }

}
