package buildin;

import java.util.logging.Level;
import java.util.logging.Logger;
import reflection.NodeDefinition;

public class Sleep implements NodeDefinition {

	@Override
	public int getInletCount() {
		return 3;
	}

	@Override
	public Class getClassForInlet(int index) {
		switch (index){ 
			case 0:
				return Long.class;
			case 1:
				return Integer.class;
			case 2:
				return Object.class;
			default:
				return null;
		}
	}

	@Override
	public String getNameForInlet(int index) {
		switch (index){ 
			case 0:
				return "Zeit in ms";
			case 1:
				return "Zeit in s";
			case 2:
				return "Data";
			default:
				return null;
		}
	}

	@Override
	public boolean isInletForArray(int index) {
		switch (index){ 
			case 0:
				return false;
			case 1:
				return false;
			case 2:
				return false;
			default:
				return false;
		}
	}

	@Override
	public int getOutletCount() {
		return 1;
	}

	@Override
	public Class getClassForOutlet(int index) {
		switch (index){ 
			case 0:
				return Object.class;
			default:
				return null;
		}
	}

	@Override
	public String getNameForOutlet(int index) {
		switch (index){ 
			case 0:
				return "Data";
			default:
				return null;
		}
	}

	@Override
	public boolean isOutletForArray(int index) {
		switch (index){ 
			case 0:
				return false;
			default:
				return false;
		}
	}

	@Override
	public String getName() {
		return "Schlafen";
	}

	@Override
	public String getDescription() {
		return "Dieses Element schläft eine angegebene Zeit und gibt dann beliebiges Objekt weiter.\nAlternativ kann auch der Auslöser verwendet werden um ein weiteres Element anzustossen. Werden Zeiten in verschiedenen Einheiten angegeben so werden diese addiert.";
	}

	@Override
	public String getUniqueName() {
		return "buildin.Sleep";
	}

	@Override
	public String getIconName() {
		return "Sand-watch_30px.png";
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void run(InOut io) {
		
		Long zeitInMS = (Long) io.in0(0);
		Integer zeitInS = (Integer) io.in0(1);
		Object data = io.in0(2, new Object[0]);
                
                if(zeitInMS == null) {
                    zeitInMS = 0l;
                }
                if(zeitInS != null) {
                    zeitInMS += zeitInS * 1000;
                }
		
                try {
                    Thread.sleep(zeitInMS);
                } catch (InterruptedException ex) {}
		
                io.out(0, data);
	}

}

