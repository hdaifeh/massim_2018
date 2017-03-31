package massim.monitor;

import massim.protocol.WorldData;
import massim.protocol.scenario.city.data.DynamicCityData;
import massim.protocol.scenario.city.data.StaticCityData;

import org.webbitserver.WebServers;
import org.webbitserver.WebServer;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.handler.HttpToWebSocketHandler;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

/**
 * The web monitor for the MASSim server.
 */
public class Monitor {

    private final SocketHandler socketHandler = new SocketHandler();

    /**
     * Constructor.
     * Used by the massim server to create the "live" monitor.
     */
    public Monitor() throws ExecutionException, InterruptedException {
        WebServer server = WebServers.createWebServer(7777)
            .add("/socket", new HttpToWebSocketHandler(this.socketHandler))
            .add(new StaticFileHandler("webmonitor/www"))
            .start()
            .get();
    }

    /**
     * Creates a new monitor to watch replays with.
     * @param replayPath the path to a replay file
     */
    Monitor(String replayPath){
        // TODO
    }

    /**
     * Updates the current state of the monitor.
     * Called by the massim server after each step.
     */
    public void updateState(WorldData worldData){
        if (worldData instanceof StaticCityData) {
            updateStaticCityData((StaticCityData) worldData);
        } else if (worldData instanceof DynamicCityData){
            updateDynamicCityData((DynamicCityData) worldData);
        } else {
            System.out.println("Monitor: wrong scenario");
            return;
        }
    }

    private void updateStaticCityData(StaticCityData data) {
        JSONObject d = new JSONObject();
        d.put("simId", data.simId);
        d.put("steps", data.steps);
        d.put("map", data.map);
        d.put("seedCapital", data.seedCapital);
        d.put("teams", data.teams);
        d.put("roles", data.roles);
        d.put("items", data.items);
        this.socketHandler.broadcast(d.toString());
    }

    private void updateDynamicCityData(DynamicCityData data) {
        JSONObject d = new JSONObject();
        d.put("workshops", data.workshops);
        d.put("chargingStations", data.chargingStations);
        d.put("shops", data.shops);
        d.put("dumps", data.dumps);
        d.put("resourceNodes", data.resourceNodes);
        d.put("storages", data.storages);
        d.put("entities", data.entities);
        this.socketHandler.broadcast(d.toString());
    }
}
