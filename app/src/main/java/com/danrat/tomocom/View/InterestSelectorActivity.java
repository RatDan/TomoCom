package com.danrat.tomocom.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.danrat.tomocom.Adapter.InterestAdapter;
import com.danrat.tomocom.Model.Interest;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.InterestsViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InterestSelectorActivity extends AppCompatActivity {

    private InterestsViewModel interestsViewModel;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selector);

        interestsViewModel = new ViewModelProvider(this).get(InterestsViewModel.class);
        Button nextButton = findViewById(R.id.nextButton);
        mode = getIntent().getStringExtra("mode");
        if (mode != null)
            nextButton.setText(R.string.button_save);

        List<Interest> interests = Arrays.asList(
                new Interest("Taniec", Arrays.asList("Taniec współczesny", "Balet", "Hip-hop", "Salsa", "Bachata", "Breakdance", "Tango")),
                new Interest("Moda", Arrays.asList("Moda uliczna", "Haute couture", "Moda ekologiczna", "Akcesoria", "Stylizacja włosów", "Stylizacja paznokci", "Projektowanie mody")),
                new Interest("Przedstawienia", Arrays.asList("Teatr", "Musicale", "Kabaret", "Stand-up", "Przedstawienia cyrkowe", "Opera", "Balet")),
                new Interest("Podróżowanie", Arrays.asList("Podróże przygodowe", "Turystyka kulturowa", "Turystyka kulinarna", "Turystyka sportowa", "Ekoturystyka", "Podróże z plecakiem", "Rejsy")),
                new Interest("Sztuka", Arrays.asList("Malarstwo", "Rzeźba", "Fotografia artystyczna", "Rysunek", "Grafika cyfrowa", "Sztuka uliczna", "Tkanina artystyczna")),
                new Interest("Gry", Arrays.asList("Fabularne", "RPG", "Mobilne", "Gacha", "Przygodowe", "Metroidvania", "Open-World", "Rytmiczne", "Turowe")),
                new Interest("Sport", Arrays.asList("Piłka nożna", "Koszykówka", "Siatkówka", "Tenis", "Pływanie", "Bieganie", "Rower górski")),
                new Interest("Zwierzęta", Arrays.asList("Psy", "Koty", "Akwarystyka", "Ptaki domowe", "Jeździectwo", "Terrarystyka", "Zwierzęta egzotyczne")),
                new Interest("Gotowanie", Arrays.asList("Kuchnia włoska", "Kuchnia azjatycka", "Wypieki", "Kuchnia wegetariańska/wegańska", "Grillowanie", "Desery", "Kuchnia molekularna")),
                new Interest("Filmy", Arrays.asList("Filmy akcji", "Filmy romantyczne", "Filmy science-fiction", "Filmy fantasy", "Dokumentalne", "Horrory", "Animacje")),
                new Interest("Anime i Manga", Arrays.asList("Shōnen", "Shōjo", "Josei", "Seinen", "Mecha", "Isekai", "Slice of Life", "Ecchi", "Historyczne", "Fantasy", "Komedia")),
                new Interest("Muzyka", Arrays.asList("Rock", "Jazz", "Muzyka klasyczna", "Hip-hop", "EDM", "Muzyka filmowa", "Pop")),
                new Interest("Programowanie", Arrays.asList("Programowanie frontendowe", "Programowanie backendowe", "Programowanie mobilne", "Programowanie gier", "Sztuczna inteligencja")),
                new Interest("Przyroda", Arrays.asList("Obserwowanie ptaków", "Ochrona środowiska", "Botanika", "Fotografia przyrodnicza", "Ogrodnictwo", "Parki narodowe", "Wspinaczka górska")),
                new Interest("Gry Online", Arrays.asList("MMORPG", "MOBA", "FPS", "Gry strategiczne", "Gry survivalowe", "Battle royale", "Gry symulacyjne")),
                new Interest("Siłownia", Arrays.asList("Trening siłowy", "Trening cardio", "CrossFit", "Trening funkcjonalny", "Powerlifting", "Kulturystyka", "Trening personalny")),
                new Interest("Pisarstwo", Arrays.asList("Pisanie powieści", "Pisanie poezji", "Pisanie scenariuszy", "Pisanie artykułów", "Tworzenie blogów", "Ghostwriting", "Twórcze pisanie")),
                new Interest("Fitness", Arrays.asList("Joga", "Pilates", "Zumba", "HIIT", "Stretching", "Aerobik", "Trening obwodowy")),
                new Interest("Aktywność na zewnątrz", Arrays.asList("Bieganie", "Jazda na rowerze", "Kajakarstwo", "Wspinaczka", "Survival", "Paintball", "Obserwacja gwiazd")),
                new Interest("Koncerty", Arrays.asList("Rock", "Jazz", "Muzyka klasyczna", "EDM", "Festiwale muzyczne", "Koncerty kameralne", "Muzyka akustyczna")),
                new Interest("Nauka", Arrays.asList("Astronomia", "Fizyka", "Chemia", "Biologia", "Psychologia", "Inżynieria", "Informatyka")),
                new Interest("Książki", Arrays.asList("Powieści fantastyczne", "Kryminały", "Powieści romantyczne", "Literatura faktu", "Literatura młodzieżowa", "Powieści historyczne", "Poezja")),
                new Interest("Kemping", Arrays.asList("Kempingi rodzinne", "Biwakowanie survivalowe", "Kempingi w górach", "Kempingi nad jeziorem", "Glamping", "Kempingi w lasach", "Kempingi zimowe")),
                new Interest("Gry planszowe", Arrays.asList("Gry strategiczne", "Gry kooperacyjne", "Gry przygodowe", "Gry rodzinne", "Gry karciane", "Gry logiczne", "Gry RPG")),
                new Interest("Wędkarstwo", Arrays.asList("Wędkarstwo spinningowe", "Wędkarstwo muchowe", "Wędkarstwo karpiowe", "Wędkarstwo morskie", "Wędkarstwo podlodowe", "Wędkarstwo sportowe", "Łowienie na spławik")),
                new Interest("Hiking", Arrays.asList("Wędrówki górskie", "Wędrówki leśne", "Trekking", "Wędrówki po wybrzeżu", "Wędrówki długodystansowe", "Nordic walking", "Wędrówki zimowe"))
        );

        ExpandableListView expandableListView = findViewById(R.id.expandableInterestList);
        InterestAdapter adapter = new InterestAdapter(this, interests);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String selectedCategory = interests.get(groupPosition).getCategory();
            String selectedSubcategory = interests.get(groupPosition).getSubcategories().get(childPosition);
            interestsViewModel.toggleInterest(selectedCategory, selectedSubcategory, ((CheckedTextView) v).isChecked());
            return true;
        });

        interestsViewModel.isButtonEnabled().observe(this, nextButton::setEnabled);

        nextButton.setOnClickListener(v -> {
            Map<String, String> selectedInterests = interestsViewModel.getSelectedCategoriesWithSubinterests().getValue();
            if (selectedInterests != null && selectedInterests.size() < 3) {
                Toast.makeText(InterestSelectorActivity.this, "Musisz wybrać minimum 3 zainteresowania", Toast.LENGTH_SHORT).show();
            } else {
                interestsViewModel.updateInterests(unused -> {
                    Log.d("InterestSelector", "Zaktualizowano pomyślnie");
                    handleActivity();
                }, e -> Log.w("InterestSelector", "Błąd przy aktualizacji", e));
            }
        });
    }

    private void handleActivity() {
        if ("update".equals(mode))
            finish();
        else if ("missing".equals(mode))
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        else {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    public InterestsViewModel getInterestsViewModel() {
        return interestsViewModel;
    }

}