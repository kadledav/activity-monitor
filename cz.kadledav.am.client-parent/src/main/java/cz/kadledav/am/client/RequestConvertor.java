package cz.kadledav.am.client;

import com.google.gson.Gson;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;

public class RequestConvertor {

	private static Gson gson = new Gson();
	private static final DynamicBooleanProperty user = DynamicPropertyFactory.getInstance().getBooleanProperty("convert.field.user", true);
	private static final DynamicBooleanProperty process = DynamicPropertyFactory.getInstance().getBooleanProperty("convert.field.process", true);
	private static final DynamicBooleanProperty title = DynamicPropertyFactory.getInstance().getBooleanProperty("convert.field.title", true);

	public static String toString(StateInfo info) {
		StateInfo fillteredInfo = new StateInfo( //
				user.getValue() ? info.getUser() : null, //
				info.getCreatedNanoTime(), //
				process.getValue() ? info.getProcess() : null, //
				title.getValue() ? info.getTitle() : null //
		);
		return gson.toJson(fillteredInfo);
	}

}
