package reflection;

public interface CompatibilityTestable {

    public class Result {

        private final String message;
        private final String userMessage;

        public Result(String message, String userMessage) {
            this.message = message;
            this.userMessage = userMessage;
        }

        public String getMessage() {
            return message;
        }

        public String getUserMessage() {
            return userMessage;
        }

    }

    public Result testForCompatibility();

}
