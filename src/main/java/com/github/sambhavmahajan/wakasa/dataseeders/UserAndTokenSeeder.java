package com.github.sambhavmahajan.wakasa.dataseeders;

import com.github.sambhavmahajan.wakasa.dto.RegisterDTO;
import com.github.sambhavmahajan.wakasa.service.AppUserService;
import com.github.sambhavmahajan.wakasa.service.TokenService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Profile("data-seeder")
@AllArgsConstructor
public class UserAndTokenSeeder implements CommandLineRunner {
    private static final String names[] = {"Jailene","Morris","Luca","Brisa","Arjun","Miguelangel","Mackenna","Jackie","Nikolas","Jacie","Hope","Tiarra","Carlo","Saira","Dante","Dakoda","Natalya","Dominik","Carlos","Jaeden","Coby","Conrad","Griffin","Emanuel","Sean","Ann","Francisca","Daylon","Rosa","Carrington","Shianne","Remington","Hailee","Isaac","Rhett","Dalia","Anna","Brycen","Armani","Sydni","Alecia","Denver","Kasandra","Shayne","Skylar","Farrah","Destinie","Sheila","Tiera","Heath","Sanjana","Kaylea","Rylee","Uriah","Zackary","Litzy","Amari","Salvador","Brenda","Jensen","Bernard","Kala","Edith","Kellie","Jordan","Marley","Paris","Tyriq","Paola","Weston","Elisha","Bernardo","Loren","Magaly","Emerson","Devan","Alannah","Heriberto","Annemarie","Rick","Regan","Salena","Marisela","Cathryn","Mordechai","Tayler","Freddy","Gabriella","Victoria","Naya","Maureen","Judah","Genesis","Reagan","Kassidy","Britany","Amina","Cloe","Kendall","Garrett"};
    private final AppUserService appUserService;
    @Override
    public void run(String... args) {
        int count = 0;
        for (String name : names) {
            for (String s : names) {
                RegisterDTO dto = new RegisterDTO(name + s + "@email.com", "123456Qw", "123456Qw");
                appUserService.createNewUser(dto);
                count++;
            }
            System.out.println(count);
        }
        System.out.println("seeding complete");
    }
}
