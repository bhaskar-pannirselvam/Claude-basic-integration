# Claude-basic-integration

A simple example project for trying out Claude integration.

## Setup

1. Install Maven if not already installed.

2. Install dependencies:

   ```bash
   mvn clean install
   ```

3. Set your Claude API key in the environment:

   ```bash
   export CLAUDE_API_KEY="your_api_key_here"
   ```

## Run

You can run the example program with a prompt argument:

```bash
mvn exec:java -Dexec.mainClass="Main" -Dexec.args="Write a short haiku about code reviews."
```

Or run it without arguments and enter a prompt interactively:

```bash
mvn exec:java -Dexec.mainClass="Main"
```

## How it works

The `Main.java` class sends a prompt to the Claude API using Java's HttpClient and prints the model's response.

- `CLAUDE_API_KEY` is read from the environment
- `Main.java` builds a Claude-style dialogue prompt
- the result is printed to stdout
