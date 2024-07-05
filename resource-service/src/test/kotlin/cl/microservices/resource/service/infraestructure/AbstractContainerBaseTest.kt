//package cl.microservices.resource.service.infraestructure
//
//import dasniko.testcontainers.keycloak.KeycloakContainer
//import org.hamcrest.SelfDescribing
//import org.testcontainers.containers.PostgreSQLContainer
//import org.springframework.test.context.DynamicPropertySource
//import org.springframework.test.context.DynamicPropertyRegistry
//import org.testcontainers.utility.DockerImageName
//import org.testcontainers.utility.MountableFile
//
//abstract class AbstractContainerBaseTest {
//    companion object {
//
//        val KEYCLOAK_IMAGE: String = "quay.io/keycloak/keycloak:23.0.1"
//        val keycloakContainer:KeycloakContainer
//        val postgreSQLContainer = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:16-alpine")).apply {
//            withUsername("keycloak")
//            withPassword("keycloak")
//            copyFileToContainer(MountableFile.forHostPath("../../../infraestructure/volumes/postgres"), "")
//        }
//        init {
//            keycloakContainer = KeycloakContainer(KEYCLOAK_IMAGE);
//            keycloakContainer.withAdminUsername("admin")
//                .withAdminPassword("admin")
//                .withEnv("DB_VENDOR", "POSTGRES")
//                .withEnv("DB_ADDR", "postgres")
//                .withEnv("DB_DATABASE", "keycloak")
//                .withEnv("DB_USER", "keycloak")
//                .withEnv("DB_SCHEMA", "public")
//                .withEnv("DB_PASSWORD", "keycloak")
//                .start();
//        }
//        @DynamicPropertySource
//        fun dynamicPropertySource(registry:DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", postgreSQLContainer)
//            registry.add("spring.datasource.username", postgreSQLContainer.username)
//            registry.add("spring.datasource.password", postgreSQLContainer.password)
//        }
//    }
//}