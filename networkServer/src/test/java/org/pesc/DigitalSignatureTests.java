package org.pesc;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesc.cds.service.PKIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.InputStream;
import java.security.KeyPair;

import static org.junit.Assert.assertTrue;


@ActiveProfiles({"h2"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DigitalSignatureTests {

	@Autowired
	PKIService pkiService;

	/*@ClassRule
	public static DockerContainerRule dockerContainerRule = new DockerContainerRule("networkserver_db_image");*/


	@Test
	public void testDocumentSigning() {

		KeyPair keyPair = pkiService.getSigningKeys();

		InputStream inputStream = getClass().getResourceAsStream("/test.txt");

		byte[] signature = pkiService.createDigitalSignature(inputStream, keyPair.getPrivate());

		assertTrue("File signatures do not match.", pkiService.verifySignature(getClass().getResourceAsStream("/test.txt"), signature, keyPair.getPublic()));

	}

}
