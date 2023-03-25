package display;

import java.util.Properties;

public class TerminalDisplay implements Display{

    @Override
    public boolean outputInfo(String toDisplay, Properties otherSettings) {

        return false;
    }

    @Override
    public boolean inputInfo(String gotInfo, Properties otherSettings) {
        
        return false;
    }
}
