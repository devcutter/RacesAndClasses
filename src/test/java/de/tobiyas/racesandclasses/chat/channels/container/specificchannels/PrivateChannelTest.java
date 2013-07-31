package de.tobiyas.racesandclasses.chat.channels.container.specificchannels;

import de.tobiyas.racesandclasses.chat.channels.container.AbstractChannelContainerTest;
import de.tobiyas.racesandclasses.chat.channels.container.ChannelContainer;
import de.tobiyas.racesandclasses.util.chat.ChannelLevel;

public class PrivateChannelTest extends AbstractChannelContainerTest {

	@Override
	protected ChannelContainer generateSut() {
		return new ChannelContainer(channelName, ChannelLevel.PrivateChannel);
	}

}
