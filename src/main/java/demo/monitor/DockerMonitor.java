package demo.monitor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;

import java.util.List;

public class DockerMonitor {

    public static void main(String[] args) {
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://socat:2375").build();

        while (true) {
            try {
                List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
                System.out.println("Checking " + containers.size() + " containers...");
                if (containers.size() == 0) {
                    System.out.println("No containers found. Exiting...");
                    break;
                }
                if (containers.size() != 0) {
                    System.out.println("Containers found. Checking health...");
                }
                for (Container container : containers) {
                    InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
                    InspectContainerResponse.ContainerState state = containerInfo.getState();
                    System.out.println("Container " + container.getId() + " state: " + state.getStatus());
                    if (state.getHealth() != null && !"healthy".equals(state.getHealth().getStatus())) {
                        System.out.println("Container " + container.getId() + " is not healthy. Restarting...");
                        dockerClient.restartContainerCmd(container.getId()).exec();
                    } else if (!state.getRunning()) {
                        System.out.println("Container " + container.getId() + " is stopped. Restarting...");
                        dockerClient.restartContainerCmd(container.getId()).exec();
                    }
                }
                Thread.sleep(3000); // Check every 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}