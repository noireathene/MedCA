package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.databinding.SearchpageBinding;
import com.example.medca.listeners.MedicineListener;
import com.example.medca.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class searchpage extends AppCompatActivity implements MedicineListener {
    RecyclerView medRV;
   private ArrayList<MedicineModal> medicineModalArrayList;
    SearchPageAdapter adapter;
    SearchpageBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpage);

        medRV = findViewById(R.id.recyclerView2);

        buildRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }



    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), prescriptionpage.class);
        startActivity(intent);
    }

    @Override
    public void onClick(ArrayList<MedicineModal> medicines) {
        Intent intent = new Intent(getApplicationContext(), prescriptionpage.class);
        startActivity(intent);
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<MedicineModal> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (MedicineModal item : medicineModalArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getMedicineName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }else if (item.getGenName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
           // Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }
    private void buildRecyclerView() {

        // below line we are creating a new array list
        medicineModalArrayList = new ArrayList<>();

        // below line is to add data to our array list.
        medicineModalArrayList.add(new MedicineModal("Miochol-E", "Acetylcholine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Bethanecol "));
        medicineModalArrayList.add(new MedicineModal("Urecholine ", "Bethanecol "));
        medicineModalArrayList.add(new MedicineModal("Carbachol ", "Carbachol "));
        medicineModalArrayList.add(new MedicineModal("Isopto Carbachol ", "Carbacol (topical) "));
        medicineModalArrayList.add(new MedicineModal("Carboptic ", "Carbachol (topical) "));
        medicineModalArrayList.add(new MedicineModal("Miostat ", "Ophthalmic (intraocular) "));
        medicineModalArrayList.add(new MedicineModal("Carbastat ", "Ophthalmic (intraocular) "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Cevimeline "));
        medicineModalArrayList.add(new MedicineModal("Evoxac ", "Cevimeline "));
        medicineModalArrayList.add(new MedicineModal("Nicotine ", "Nicotine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Transdermal "));
        medicineModalArrayList.add(new MedicineModal("Nicoderm CQ ", "Transdermal "));
        medicineModalArrayList.add(new MedicineModal("Nicotrol ", "Transdermal "));
        medicineModalArrayList.add(new MedicineModal("Pilocarpine ", "Pilocarpine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ophthalmic (drops) 1, 2, 4, 6 "));
        medicineModalArrayList.add(new MedicineModal("Isopto Carpine ", "Ophthalmic (drops) 1, 2, 4, 6 "));
        medicineModalArrayList.add(new MedicineModal("Ocusert Pilo-20 ", "Ophthalmic sustained-release inserts "));
        medicineModalArrayList.add(new MedicineModal("Ocusert Pilo-40 ", "Ophthalmic sustained-release inserts "));
        medicineModalArrayList.add(new MedicineModal("Salagen ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Chantix ", "Varenicline "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Donepezil "));
        medicineModalArrayList.add(new MedicineModal("Aricept ", "Donepeliz "));
        medicineModalArrayList.add(new MedicineModal("Phospholine ", "Echothiophate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Edrophonium "));
        medicineModalArrayList.add(new MedicineModal("Tensilon ", "Edrophonium "));
        medicineModalArrayList.add(new MedicineModal("Generic", "Galantamine"));
        medicineModalArrayList.add(new MedicineModal("Reminyl ", "Galantamine "));
        medicineModalArrayList.add(new MedicineModal("Razadyne", "Galantamine"));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Neostigmine "));
        medicineModalArrayList.add(new MedicineModal("Prostigmin ", "Neostigmine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Physostigmine "));
        medicineModalArrayList.add(new MedicineModal("Eserine ", "Physostigmine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Pyridostigmine "));
        medicineModalArrayList.add(new MedicineModal("Mestinon ", "Pyridostigmine "));
        medicineModalArrayList.add(new MedicineModal("Regonol ", "Pyridostigmine "));
        medicineModalArrayList.add(new MedicineModal("Exelon ", "Pyridostigmine "));
        medicineModalArrayList.add(new MedicineModal("Tudorsa Pressair ", "Aclidinium "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Atropine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Belladonna Alkaloids "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Extract "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Tincture "));
        medicineModalArrayList.add(new MedicineModal("Botox ", "Botulinum Toxin A "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Clidinium "));
        medicineModalArrayList.add(new MedicineModal("Quarzan ", "Clidinium "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Cyclopentolate "));
        medicineModalArrayList.add(new MedicineModal("Cyclogyl ", "Cyclopentolate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Darifenacin "));
        medicineModalArrayList.add(new MedicineModal("Enablex ", "Darifenacin "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Dicyclomine "));
        medicineModalArrayList.add(new MedicineModal("Bentyl ", "Dicyclomine "));
        medicineModalArrayList.add(new MedicineModal("Toviaz ", "Fesoterodine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Flavoxate "));
        medicineModalArrayList.add(new MedicineModal("Urispas ", "Flavoxate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Glycopyrrolate "));
        medicineModalArrayList.add(new MedicineModal("Robinul (systemic) ", "Glycopyrrolate "));
        medicineModalArrayList.add(new MedicineModal("Seebri Neohaler (oral inhalation) ", "Glycopyrrolate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Homatropine "));
        medicineModalArrayList.add(new MedicineModal("Isopto Homatropine ", "Homatropine "));
        medicineModalArrayList.add(new MedicineModal("Anaspaz ", "l-Hyoscyamine "));
        medicineModalArrayList.add(new MedicineModal("Cystospaz-M ", "l-Hyoscyamine "));
        medicineModalArrayList.add(new MedicineModal("Levsin ", "l-Hyoscyamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ipatropium "));
        medicineModalArrayList.add(new MedicineModal("Atrovent ", "Ipatropium "));
        medicineModalArrayList.add(new MedicineModal("Cantil ", "Mepenzolate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Methscopolamine "));
        medicineModalArrayList.add(new MedicineModal("Pamine ", "Methscopolamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Oxybutinin "));
        medicineModalArrayList.add(new MedicineModal("Ditropan ", "Oxybutinin "));
        medicineModalArrayList.add(new MedicineModal("Gelnique ", "Oxybutinin "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Propantheline "));
        medicineModalArrayList.add(new MedicineModal("Pro-Bantine ", "Propantheline "));
        medicineModalArrayList.add(new MedicineModal("Scopolamine ", "Scopolamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Isopto Hyoscine ", "Opththalmic "));
        medicineModalArrayList.add(new MedicineModal("Transderm Scop ", "Transdermal "));
        medicineModalArrayList.add(new MedicineModal("Vesicare ", "Solifenacin "));
        medicineModalArrayList.add(new MedicineModal("Spiriva ", "Tiotropium "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Tolterodine "));
        medicineModalArrayList.add(new MedicineModal("Detrol ", "Tolterodine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Tropicamide "));
        medicineModalArrayList.add(new MedicineModal("Mydriacyl Opththalmic ", "Tropicamide "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Trospium "));
        medicineModalArrayList.add(new MedicineModal("Sanctura ", "Trospium "));
        medicineModalArrayList.add(new MedicineModal("Incruse Ellipta ", "Umeclidinium "));
        medicineModalArrayList.add(new MedicineModal("Vecamyl ", "Mecamylamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Pralidoxime "));
        medicineModalArrayList.add(new MedicineModal("Protopam ", "Pralidoxime "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Amphetamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Racemic Mixture "));
        medicineModalArrayList.add(new MedicineModal("Adderall ", "Amphetamine Sulfate "));
        medicineModalArrayList.add(new MedicineModal("Adderall ", "Amphetamine  Aspartate"));
        medicineModalArrayList.add(new MedicineModal("Adderall ", "Dextroamphetamine Sulfate "));
        medicineModalArrayList.add(new MedicineModal("Adderall ", "Dextroamphetamine Saccharate"));
        medicineModalArrayList.add(new MedicineModal("Iopidine ", "Apraclonidine "));
        medicineModalArrayList.add(new MedicineModal("Nuvigil ", "Armodafinil "));
        medicineModalArrayList.add(new MedicineModal("Alphagan ", "Brimonidine "));
        medicineModalArrayList.add(new MedicineModal("Precedex ", "Dexmedetomidine "));
        medicineModalArrayList.add(new MedicineModal("Focalin ", "Dexmethylphenidate "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Dextroamphetamine "));
        medicineModalArrayList.add(new MedicineModal("Dexedrine ", "Dextroamphetamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Dobutamine "));
        medicineModalArrayList.add(new MedicineModal("Dobutrex ", "Dobutamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Dopamine "));
        medicineModalArrayList.add(new MedicineModal("Intropin ", "Dopamine "));
        medicineModalArrayList.add(new MedicineModal("Northera ", "Droxidopa "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ephedrine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("Adrenalin Chloride ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("Primatene Mist ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("Bronkaid Mist ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("EpiPen ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("Auvi-Q ", "Epinephrine "));
        medicineModalArrayList.add(new MedicineModal("Corlopam ", "Fenoldopam "));
        medicineModalArrayList.add(new MedicineModal("Paremyd (includes 0.25% tropicamide) ", "Hydroxyamphetamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Isoproterenol "));
        medicineModalArrayList.add(new MedicineModal("Isuprel ", "Isoproterenol "));
        medicineModalArrayList.add(new MedicineModal("Aramine ", "Metaraminol "));
        medicineModalArrayList.add(new MedicineModal("Desoxyn ", "Methamphetamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Methylphenidate "));
        medicineModalArrayList.add(new MedicineModal("Ritalin ", "Methylphenidate "));
        medicineModalArrayList.add(new MedicineModal("Ritalin-SR ", "Methylphenidate "));
        medicineModalArrayList.add(new MedicineModal("ProAmatine", "Midodrine "));
        medicineModalArrayList.add(new MedicineModal("Myrbetriq ", "Mirabegron "));
        medicineModalArrayList.add(new MedicineModal("Provigil ", "Modafinil "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Naphazoline "));
        medicineModalArrayList.add(new MedicineModal("Privine ", "Nophazoline "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Norepinephrine "));
        medicineModalArrayList.add(new MedicineModal("Levophed ", "Norepinephrine "));
        medicineModalArrayList.add(new MedicineModal("Striverdi Respimat ", "Olodaterol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Oxymetazoline "));
        medicineModalArrayList.add(new MedicineModal("Afrin ", "Oxymetazoline "));
        medicineModalArrayList.add(new MedicineModal("Neo-Synephrine (12 Hour) ", "Oxymetazoline "));
        medicineModalArrayList.add(new MedicineModal("Visine LR ", "Oxymetazoloine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Phenylephrine "));
        medicineModalArrayList.add(new MedicineModal("Neo-Synephrine ", "Phenylephrine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Pseudoephedrine "));
        medicineModalArrayList.add(new MedicineModal("Sudafed ", "Pseudoephedrine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Tetrahydrozoline "));
        medicineModalArrayList.add(new MedicineModal("Visine ", "Tetrahydrozoline "));
        medicineModalArrayList.add(new MedicineModal("Zanaflex ", "Tizanidine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Xylometazoline "));
        medicineModalArrayList.add(new MedicineModal("Otrivin ", "Xylometazoline "));
        medicineModalArrayList.add(new MedicineModal("Uroxatral ", "Alfuzosin "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Doxazosin "));
        medicineModalArrayList.add(new MedicineModal("Cardura ", "Doxazosin "));
        medicineModalArrayList.add(new MedicineModal("Dibenzyline ", "Phenoxybenzamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Phentolamine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Prazosin "));
        medicineModalArrayList.add(new MedicineModal("Minipress ", "Prazosin "));
        medicineModalArrayList.add(new MedicineModal("Rapaflo ", "Silodosin "));
        medicineModalArrayList.add(new MedicineModal("Flomax ", "Tamzsulosin "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Terazosin "));
        medicineModalArrayList.add(new MedicineModal("Hytrin ", "Terazosin "));
        medicineModalArrayList.add(new MedicineModal("Priscoline ", "Tolazoline "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Acebutolol "));
        medicineModalArrayList.add(new MedicineModal("Sectral ", "Acebutolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Atenolol "));
        medicineModalArrayList.add(new MedicineModal("Tenormin ", "Atenolol "));
        medicineModalArrayList.add(new MedicineModal("Betaxolol ", "Betaxolol "));
        medicineModalArrayList.add(new MedicineModal("Kerlone ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Betoptic ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Bisoprolol "));
        medicineModalArrayList.add(new MedicineModal("Zebeta ", "Bisoprolol "));
        medicineModalArrayList.add(new MedicineModal("Carteolol ", "Carteolol "));
        medicineModalArrayList.add(new MedicineModal("Cartrol ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Ocupress ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Coreg ", "Carvedilol "));
        medicineModalArrayList.add(new MedicineModal("Brevibloc ", "Esmolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Labetalol "));
        medicineModalArrayList.add(new MedicineModal("Normodyne ", "Labetalol "));
        medicineModalArrayList.add(new MedicineModal("Trandate ", "Labetalol "));
        medicineModalArrayList.add(new MedicineModal("Betagan Liquifilm ", "Levobunolol "));
        medicineModalArrayList.add(new MedicineModal("OptiPranolol ", "Metipranolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Metoprolol "));
        medicineModalArrayList.add(new MedicineModal("Lopressor ", "Metroprolol "));
        medicineModalArrayList.add(new MedicineModal("Toprol ", "Metroprolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Nadolol "));
        medicineModalArrayList.add(new MedicineModal("Corgard ", "Nadolol "));
        medicineModalArrayList.add(new MedicineModal("Bystolic ", "Nebivolol "));
        medicineModalArrayList.add(new MedicineModal("Levatol ", "Penbutolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Pindolol "));
        medicineModalArrayList.add(new MedicineModal("Visken ", "Pindolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Propranolol "));
        medicineModalArrayList.add(new MedicineModal("Inderal ", "Propranolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Sotalol "));
        medicineModalArrayList.add(new MedicineModal("Betapace ", "Sotalol "));
        medicineModalArrayList.add(new MedicineModal("Timolol ", "Timolol "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Biocadren ", "Oral "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Timoptic ", "Ophthalmic "));
        medicineModalArrayList.add(new MedicineModal("Demser ", "Metyrosine "));
        medicineModalArrayList.add(new MedicineModal("Generic ", "Digoxin "));
        medicineModalArrayList.add(new MedicineModal("Lanoxin ", "Digoxin "));
        medicineModalArrayList.add(new MedicineModal("Lanoxicaps ", "Digoxin "));
        medicineModalArrayList.add(new MedicineModal("Digibind ", "Digoxin Immunefab (ovine) "));
        medicineModalArrayList.add(new MedicineModal("DigiFab ", "Digoxin Immunefab (ovine) "));

        Collections.sort(medicineModalArrayList, new Comparator<MedicineModal>() {
            @Override
            public int compare(MedicineModal o1, MedicineModal o2) {
                return o1.getGenName().compareTo(o2.getGenName());
            }
        });


        // initializing our adapter class.
        adapter = new SearchPageAdapter(medicineModalArrayList, searchpage.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
       // re.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        medRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        medRV.setAdapter(adapter);
    }
}




