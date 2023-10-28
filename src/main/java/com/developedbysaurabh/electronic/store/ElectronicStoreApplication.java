package com.developedbysaurabh.electronic.store;

import com.developedbysaurabh.electronic.store.entities.Role;
import com.developedbysaurabh.electronic.store.entities.User;
import com.developedbysaurabh.electronic.store.repositories.RoleRepository;
import com.developedbysaurabh.electronic.store.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	private RoleRepository roleRepository;
	private Logger logger = LoggerFactory.getLogger(ElectronicStoreApplication.class);
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	@Value("${normal.role.id}")
	private  String normalRoleId;

	@Value("${admin.role.id}")
	private  String adminRoleId;


	//	DEFAULT SUPER USER 1 DATA

	@Value("${superuser1.name}")
	private  String super1UserName;

	@Value("${superuser1.email}")
	private  String super1Email;

	@Value("${superuser1.password}")
	private  String super1Password;

	@Value("${superuser1.gender}")
	private  String super1Gender;

	@Value("${superuser1.about}")
	private  String super1About;

	@Value("${superuser1.imageName}")
	private  String super1ImageName;

	@Value("${superuser1.userId}")
	private  String super1UserId;


//	DEFAULT SUPER USER 2 DATA

	@Value("${superuser2.name}")
	private  String super2UserName;

	@Value("${superuser2.email}")
	private  String super2Email;

	@Value("${superuser2.password}")
	private  String super2Password;

	@Value("${superuser2.gender}")
	private  String super2Gender;

	@Value("${superuser2.about}")
	private  String super2About;

	@Value("${superuser2.imageName}")
	private  String super2ImageName;

	@Value("${superuser2.userId}")
	private  String super2UserId;



	public ElectronicStoreApplication(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;

	}

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		try
		{
			Role role_admin = Role.builder().roleId(adminRoleId).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(normalRoleId).roleName("ROLE_NORMAL").build();

			HashSet<Role> adminRoles1= new HashSet<>();
			adminRoles1.add(role_admin);
			adminRoles1.add(role_normal);
			HashSet<Role> adminRoles2= new HashSet<>();
			adminRoles2.add(role_admin);
			adminRoles2.add(role_normal);

			roleRepository.save(role_admin);
			roleRepository.save(role_normal);


			User user1 = User.builder()
					.name(super1UserName)
					.email(super1Email)
					.password(passwordEncoder.encode(super1Password))
					.about(super1About)
					.imageName(super1ImageName)
					.gender(super1Gender)
					.userId(super1UserId)
					.roles(adminRoles1)
					.build();

			User user2 = User.builder()
					.name(super2UserName)
					.email(super2Email)
					.password(passwordEncoder.encode(super2Password))
					.about(super2About)
					.imageName(super2ImageName)
					.gender(super2Gender)
					.userId(super2UserId)
					.roles(adminRoles2)
					.build();

			userRepository.save(user2);
			userRepository.save(user1);

			logger.info("SUPER USERS :  {}  AND {}",user2.getEmail(),user1.getEmail());


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
