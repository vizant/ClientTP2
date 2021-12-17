import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

abstract class Strategy {

    protected final BufferedReader reader;
    protected final PrintWriter writer;

    public Strategy(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public abstract void execute() throws IOException;
}
