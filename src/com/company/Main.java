package com.company;

import java.util.Random;

public class Main {

    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefenceType;
    public static int[] heroesHealth = {260, 210, 270};
    public static int[] heroesDamage = {20, 15, 10};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic"};
    public static int roundNumber;
    public static int medicHealth = 300;
    public static int medicTreatment;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameFinished()) {
            round();
        }
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefenceType = heroesAttackType[randomIndex];
        System.out.println("Boss chose: " + bossDefenceType);
    }

    public static void round() {
        roundNumber++;
        System.out.println(roundNumber + " ROUND -------------");
        chooseBossDefence();
        bossHits();
        heroesHit();
        setMedicTreament();
        printStatistics();
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (bossHealth > 0 && heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
        if (bossHealth > 0 && medicHealth > 0) {
            if (medicHealth - bossDamage < 0) {
                medicHealth = 0;
            } else {
                medicHealth -= bossDamage;
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (bossHealth > 0 && heroesHealth[i] > 0) {
                if (bossDefenceType.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    if (bossHealth - heroesDamage[i] * coeff < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth = bossHealth - heroesDamage[i] * coeff;
                    }
                    System.out.println("Critical damage: " + heroesDamage[i] * coeff);
                } else {
                    if (bossHealth - heroesDamage[i] < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth = bossHealth - heroesDamage[i]; //  bossHealth -= heroesDamage[i];
                    }
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("\n Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int health : heroesHealth) {
            if (health > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("\n Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void printStatistics() {
        System.out.println("Boss health: " + bossHealth + " (" + bossDamage + ")");
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                System.out.println(heroesAttackType[i] + " health: "
                        + heroesHealth[i] + " (" + heroesDamage[i] + ")");
            } else {
                System.out.println(heroesAttackType[i] + " health: "
                        + heroesHealth[i] + " (" + 0 + ")");
            }
        }
        if (medicHealth > 0) {
            System.out.println("Medic health: " + medicHealth + " (" + medicTreatment + ")");
        } else {
            System.out.println("Medic health: " + medicHealth + " (" + 0 + ")");
        }

    }

    public static void setMedicTreament() {
        Random random = new Random();
        boolean isTreated = false;
        boolean isTreatNeeds = false;
        for (int health : heroesHealth) {
            if (health > 0 && health < 100) {
                isTreatNeeds = true;
                break;
            }
        }
        if (medicHealth > 0 && isTreatNeeds) {
            while (!isTreated) {
                int i = random.nextInt(heroesHealth.length);
                if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    medicTreatment = random.nextInt(11) * 10;
                    System.out.println(heroesAttackType[i] + " health before Treatment: " + heroesHealth[i]);
                    heroesHealth[i] += medicTreatment;
                    isTreated = true;
                } else medicTreatment = 0;
            }
        }
    }
}

