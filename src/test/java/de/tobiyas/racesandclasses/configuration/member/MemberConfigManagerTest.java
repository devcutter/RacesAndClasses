package de.tobiyas.racesandclasses.configuration.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tobiyas.racesandclasses.configuration.member.file.MemberConfig;
import de.tobiyas.racesandclasses.generate.plugin.GenerateRaces;
import de.tobiyas.utils.tests.generate.server.GenerateBukkitServer;

public class MemberConfigManagerTest {

	private MemberConfigManager sut;

	
	@BeforeClass
	public static void init(){
		GenerateBukkitServer.generateServer();
		GenerateRaces.generateRaces();
	}
	
	
	@Before
	public void before(){
		sut = new MemberConfigManager();
	}
	
	
	@AfterClass
	public static void teardown(){
		GenerateRaces.dropMock();
		GenerateBukkitServer.dropServer();
	}
	
	@Test
	public void creating_and_retrieving_config_works(){
		String testPlayer = "playerName";
		MemberConfig config = sut.getConfigOfPlayer(testPlayer);
		
		assertNotNull(config);
		
		assertEquals("Global", config.getCurrentChannel());
	}
	
	@Test
	public void saving_config_manager_works(){
		sut.getConfigOfPlayer("player1");
		sut.getConfigOfPlayer("player2");
		
		sut.saveConfigs();
		//TODO check if saving worked.
	}
}
