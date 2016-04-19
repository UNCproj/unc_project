package beans;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Created by Денис on 10.04.2016.
 */
@Startup
@Singleton
public class ESClientsProvider {
    private Node node;
    private Client client;

    @PostConstruct
    private void createClientInstance() {
        this.node = nodeBuilder()
                    .settings(Settings.settingsBuilder().put("http.enabled", false))
                    .client(true)
                    .node();

        this.client = this.node.client();
    }

    @PreDestroy
    private void cleanUpResources() {
        this.client.close();
        this.node.close();
    }

    public Client getClient() {
        return client;
    }
}
