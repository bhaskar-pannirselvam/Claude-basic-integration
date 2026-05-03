

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;


public class Main {


    public static void main(String[] args) {
        AnthropicClient client = AnthropicOkHttpClient.fromEnv();
        
        MessageCreateParams params = MessageCreateParams.builder()
                .model("claude-sonnet-4-6")
                .maxTokens(1000)
                .addUserMessage("What ai model are you, are you claude sonnet? which version?")
                .build();

        Message message = client.messages().create(params);
        System.out.println(message.content());
    }



}