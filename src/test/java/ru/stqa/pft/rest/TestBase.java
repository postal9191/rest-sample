package ru.stqa.pft.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.testng.SkipException;

import java.io.IOException;

public class TestBase {

    private Executor getExecutor() {
        return Executor.newInstance().auth("f2c01c8399d967884390d1e87b46b533", "");
    }

    public boolean isIssueOpen(int issueId) throws IOException {
        String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues/" + issueId + ".json")).returnContent().asString();
        JsonElement parsed = new JsonParser().parse(json);
        String issueStateName = parsed.getAsJsonObject().getAsJsonArray("issues").get(0).getAsJsonObject().get("state_name").getAsString();
        return !(issueStateName.equals("Closed"));
    }

    public void skipIfNotFixed(int issueId) throws IOException {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }
}
