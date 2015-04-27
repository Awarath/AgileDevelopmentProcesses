package se.chalmers.group8.communication.http;

/**
 * Implement ConnectorResult to process the result of a HTTP request when the request is finished.
 */
public interface ConnectorResult {
    public void onConnectorResult(String result);
}
