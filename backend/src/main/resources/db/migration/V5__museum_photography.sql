-- Photography for each museum, from Wikimedia Commons.
--
-- Every image shows the actual building. Each museum was resolved through its
-- Wikidata item (P18), and a candidate was accepted only when the item's own
-- coordinates fall within 30 km of Helsinki - so a same-named museum elsewhere
-- cannot slip in. Both URLs came from the Commons API rather than being built by
-- hand: the CDN rejects thumbnail paths it did not mint, so a hand-edited width
-- returns 400.
--
-- These are mostly CC BY-SA, which requires naming the photographer and licence
-- wherever the picture is shown, so the attribution is stored with the URL and
-- rendered on both the cards and the detail page.
--
-- Museums with no match keep NULL and fall back to the generated cover.

ALTER TABLE museums ADD COLUMN image_url_large   VARCHAR(500);
ALTER TABLE museums ADD COLUMN image_credit      VARCHAR(200);
ALTER TABLE museums ADD COLUMN image_license     VARCHAR(60);
ALTER TABLE museums ADD COLUMN image_license_url VARCHAR(300);
ALTER TABLE museums ADD COLUMN image_source_url  VARCHAR(500);

ALTER TABLE museums ALTER COLUMN image_url TYPE VARCHAR(500);

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0d/Art_museum_Ateneum_in_Kluuvi%2C_Helsinki%2C_Finland%2C_2014.jpg/960px-Art_museum_Ateneum_in_Kluuvi%2C_Helsinki%2C_Finland%2C_2014.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0d/Art_museum_Ateneum_in_Kluuvi%2C_Helsinki%2C_Finland%2C_2014.jpg/1280px-Art_museum_Ateneum_in_Kluuvi%2C_Helsinki%2C_Finland%2C_2014.jpg',
    image_credit      = 'Finnish National Gallery',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Art_museum_Ateneum_in_Kluuvi,_Helsinki,_Finland,_2014.jpg'
WHERE slug = 'ateneum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0b/Kiasmamodernartmuseum.JPG/960px-Kiasmamodernartmuseum.JPG',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0b/Kiasmamodernartmuseum.JPG/1280px-Kiasmamodernartmuseum.JPG',
    image_credit      = 'Paasikivi',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Kiasmamodernartmuseum.JPG'
WHERE slug = 'kiasma';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/a/a0/Lasipalatsi.jpg/960px-Lasipalatsi.jpg',
    image_url_large   = 'https://upload.wikimedia.org/wikipedia/commons/a/a0/Lasipalatsi.jpg',
    image_credit      = 'Mahlum',
    image_license     = 'Public domain',
    image_license_url = NULL,
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Lasipalatsi.jpg'
WHERE slug = 'amos-rex';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/db/Tennispalatsi_Helsinki.jpg/960px-Tennispalatsi_Helsinki.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/db/Tennispalatsi_Helsinki.jpg/1280px-Tennispalatsi_Helsinki.jpg',
    image_credit      = 'Arkkipuudeli',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Tennispalatsi_Helsinki.jpg'
WHERE slug = 'ham';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/c/c3/Designmuseo_2019.jpg/960px-Designmuseo_2019.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/c/c3/Designmuseo_2019.jpg/1280px-Designmuseo_2019.jpg',
    image_credit      = 'Vadelmavene',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Designmuseo_2019.jpg'
WHERE slug = 'design-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b0/Arkkitehtuurimuseo_Helsinki_2022-09-18_01.jpg/960px-Arkkitehtuurimuseo_Helsinki_2022-09-18_01.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b0/Arkkitehtuurimuseo_Helsinki_2022-09-18_01.jpg/1280px-Arkkitehtuurimuseo_Helsinki_2022-09-18_01.jpg',
    image_credit      = 'Leonhard Lenz',
    image_license     = 'CC0',
    image_license_url = 'http://creativecommons.org/publicdomain/zero/1.0/deed.en',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Arkkitehtuurimuseo_Helsinki_2022-09-18_01.jpg'
WHERE slug = 'architecture-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/9a/FinlandNationalMuseum.jpg/960px-FinlandNationalMuseum.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/9a/FinlandNationalMuseum.jpg/1280px-FinlandNationalMuseum.jpg',
    image_credit      = 'Thermos',
    image_license     = 'CC BY-SA 2.5',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/2.5',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:FinlandNationalMuseum.jpg'
WHERE slug = 'national-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/8/8f/Kaupunginmuseo2.jpg/960px-Kaupunginmuseo2.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/8/8f/Kaupunginmuseo2.jpg/1280px-Kaupunginmuseo2.jpg',
    image_credit      = 'Laurakakkonen',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Kaupunginmuseo2.jpg'
WHERE slug = 'helsinki-city-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0a/Natural_History_Museum_of_Helsinki_01a.jpg/960px-Natural_History_Museum_of_Helsinki_01a.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0a/Natural_History_Museum_of_Helsinki_01a.jpg/1280px-Natural_History_Museum_of_Helsinki_01a.jpg',
    image_credit      = 'Sinikka Halme',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Natural_History_Museum_of_Helsinki_01a.jpg'
WHERE slug = 'natural-history-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5d/19-04-28-Seurasaari_0582.jpg/960px-19-04-28-Seurasaari_0582.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5d/19-04-28-Seurasaari_0582.jpg/1280px-19-04-28-Seurasaari_0582.jpg',
    image_credit      = 'Ralf Roletschek',
    image_license     = 'FAL',
    image_license_url = 'http://artlibre.org/licence/lal/en',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:19-04-28-Seurasaari_0582.jpg'
WHERE slug = 'seurasaari';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/fb/Suomenlinna_visitor_centre.jpg/960px-Suomenlinna_visitor_centre.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/fb/Suomenlinna_visitor_centre.jpg/1280px-Suomenlinna_visitor_centre.jpg',
    image_credit      = 'The Cosmonaut',
    image_license     = 'CC BY-SA 2.5 ca',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/2.5/ca/deed.en',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Suomenlinna_visitor_centre.jpg'
WHERE slug = 'suomenlinna-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b1/Sotamuseon_Maneesi.jpg/1280px-Sotamuseon_Maneesi.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b1/Sotamuseon_Maneesi.jpg/1280px-Sotamuseon_Maneesi.jpg',
    image_credit      = 'Kuusim',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Sotamuseon_Maneesi.jpg'
WHERE slug = 'military-museum-maneesi';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/55/Sinebrychoffin_taidemuseo.jpg/960px-Sinebrychoffin_taidemuseo.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/55/Sinebrychoffin_taidemuseo.jpg/1280px-Sinebrychoffin_taidemuseo.jpg',
    image_credit      = 'Samuli Lintula',
    image_license     = 'CC BY 2.5',
    image_license_url = 'https://creativecommons.org/licenses/by/2.5',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Sinebrychoffin_taidemuseo.jpg'
WHERE slug = 'sinebrychoff';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/6/6f/Kaapelitehdas_2010.jpg/960px-Kaapelitehdas_2010.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/6/6f/Kaapelitehdas_2010.jpg/1280px-Kaapelitehdas_2010.jpg',
    image_credit      = 'Gregorius',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Kaapelitehdas_2010.jpg'
WHERE slug = 'photography-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/91/Kaapelitehdas_Helsinki_Finland_2007.jpg/960px-Kaapelitehdas_Helsinki_Finland_2007.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/91/Kaapelitehdas_Helsinki_Finland_2007.jpg/1280px-Kaapelitehdas_Helsinki_Finland_2007.jpg',
    image_credit      = 'Khaosaming',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'http://creativecommons.org/licenses/by-sa/3.0/',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Kaapelitehdas_Helsinki_Finland_2007.jpg'
WHERE slug = 'hotel-restaurant-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/8/8b/Vanhakaupunki_vesiputous_2009.jpg/960px-Vanhakaupunki_vesiputous_2009.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/8/8b/Vanhakaupunki_vesiputous_2009.jpg/1280px-Vanhakaupunki_vesiputous_2009.jpg',
    image_credit      = 'Matti Paavonen',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Vanhakaupunki_vesiputous_2009.jpg'
WHERE slug = 'technology-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/00/Ratikkamuseo_2025-9-Marit_Henriksson.jpg/960px-Ratikkamuseo_2025-9-Marit_Henriksson.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/00/Ratikkamuseo_2025-9-Marit_Henriksson.jpg/1280px-Ratikkamuseo_2025-9-Marit_Henriksson.jpg',
    image_credit      = 'Marit Henriksson',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Ratikkamuseo_2025-9-Marit_Henriksson.jpg'
WHERE slug = 'tram-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b4/Hakasalmen_huvila-Marit_Henriksson.jpg/960px-Hakasalmen_huvila-Marit_Henriksson.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b4/Hakasalmen_huvila-Marit_Henriksson.jpg/1280px-Hakasalmen_huvila-Marit_Henriksson.jpg',
    image_credit      = 'Marit Henriksson',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Hakasalmen_huvila-Marit_Henriksson.jpg'
WHERE slug = 'villa-hakasalmi';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/4/4c/Ruiskumestarin_talon_piharakennus_-_Marit_Henriksson.jpg/960px-Ruiskumestarin_talon_piharakennus_-_Marit_Henriksson.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/4/4c/Ruiskumestarin_talon_piharakennus_-_Marit_Henriksson.jpg/1280px-Ruiskumestarin_talon_piharakennus_-_Marit_Henriksson.jpg',
    image_credit      = 'Marit Henriksson',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Ruiskumestarin_talon_piharakennus_-_Marit_Henriksson.jpg'
WHERE slug = 'burghers-house';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1f/Taidehalli_Helsinki.jpg/960px-Taidehalli_Helsinki.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1f/Taidehalli_Helsinki.jpg/1280px-Taidehalli_Helsinki.jpg',
    image_credit      = 'MyName (Mahlum)',
    image_license     = 'Public domain',
    image_license_url = NULL,
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Taidehalli_Helsinki.jpg'
WHERE slug = 'taidehalli';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/03/Didrichsenin_taidemuseo.jpg/960px-Didrichsenin_taidemuseo.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/0/03/Didrichsenin_taidemuseo.jpg/1280px-Didrichsenin_taidemuseo.jpg',
    image_credit      = 'Ppntori',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Didrichsenin_taidemuseo.jpg'
WHERE slug = 'didrichsen';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1c/Villa_Gyllenberg_2.jpg/960px-Villa_Gyllenberg_2.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/1/1c/Villa_Gyllenberg_2.jpg/1280px-Villa_Gyllenberg_2.jpg',
    image_credit      = 'Ppntori',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Villa_Gyllenberg_2.jpg'
WHERE slug = 'villa-gyllenberg';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/c/c8/Mannerheim_Museum.jpg/960px-Mannerheim_Museum.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/c/c8/Mannerheim_Museum.jpg/1280px-Mannerheim_Museum.jpg',
    image_credit      = 'Tomisti',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'http://creativecommons.org/licenses/by-sa/3.0/',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Mannerheim_Museum.jpg'
WHERE slug = 'mannerheim-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/d1/Tamminiemi_%2815950987534%29.jpg/960px-Tamminiemi_%2815950987534%29.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/d1/Tamminiemi_%2815950987534%29.jpg/1280px-Tamminiemi_%2815950987534%29.jpg',
    image_credit      = 'Jukka from HELSINKI, Finland',
    image_license     = 'CC BY 2.0',
    image_license_url = 'https://creativecommons.org/licenses/by/2.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Tamminiemi_(15950987534).jpg'
WHERE slug = 'urho-kekkonen-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/3/3d/0116_Helsinki_Bank_of_Finland.jpg/960px-0116_Helsinki_Bank_of_Finland.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/3/3d/0116_Helsinki_Bank_of_Finland.jpg/1280px-0116_Helsinki_Bank_of_Finland.jpg',
    image_credit      = 'Virtual-Pano',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:0116_Helsinki_Bank_of_Finland.jpg'
WHERE slug = 'bank-of-finland-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b3/Helsinki_University_Museum.jpg/960px-Helsinki_University_Museum.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/b3/Helsinki_University_Museum.jpg/1280px-Helsinki_University_Museum.jpg',
    image_credit      = 'Welshentag',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Helsinki_University_Museum.jpg'
WHERE slug = 'helsinki-university-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/f2/Sports_Museum_of_Finland.jpg/960px-Sports_Museum_of_Finland.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/f2/Sports_Museum_of_Finland.jpg/1280px-Sports_Museum_of_Finland.jpg',
    image_credit      = 'Thermos',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Sports_Museum_of_Finland.jpg'
WHERE slug = 'sports-museum';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5e/Helsingin_observatorio_10.jpg/960px-Helsingin_observatorio_10.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/5/5e/Helsingin_observatorio_10.jpg/1280px-Helsingin_observatorio_10.jpg',
    image_credit      = 'Eteil',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Helsingin_observatorio_10.jpg'
WHERE slug = 'observatory';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/f7/HelsinkiGateUniversityGardens.jpg/960px-HelsinkiGateUniversityGardens.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/f/f7/HelsinkiGateUniversityGardens.jpg/1280px-HelsinkiGateUniversityGardens.jpg',
    image_credit      = 'Thermos',
    image_license     = 'CC BY-SA 2.5',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/2.5',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:HelsinkiGateUniversityGardens.jpg'
WHERE slug = 'botanic-garden-greenhouses';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/d4/Cygnauksen_galleria.jpg/960px-Cygnauksen_galleria.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/d/d4/Cygnauksen_galleria.jpg/1280px-Cygnauksen_galleria.jpg',
    image_credit      = 'Arkkipuudeli',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Cygnauksen_galleria.jpg'
WHERE slug = 'cygnaeus-gallery';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/a/ab/Alvar_Aallon_kotitalo.jpg/960px-Alvar_Aallon_kotitalo.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/a/ab/Alvar_Aallon_kotitalo.jpg/1280px-Alvar_Aallon_kotitalo.jpg',
    image_credit      = 'Hezzu',
    image_license     = 'CC BY-SA 4.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/4.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Alvar_Aallon_kotitalo.jpg'
WHERE slug = 'aalto-house';

UPDATE museums SET
    image_url         = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/90/Studio_Aalto_exterior_2014.jpg/960px-Studio_Aalto_exterior_2014.jpg',
    image_url_large   = 'https://thumb.wikimedia.org/wikipedia/commons/thumb/9/90/Studio_Aalto_exterior_2014.jpg/1280px-Studio_Aalto_exterior_2014.jpg',
    image_credit      = 'Trogain',
    image_license     = 'CC BY-SA 3.0',
    image_license_url = 'https://creativecommons.org/licenses/by-sa/3.0',
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Studio_Aalto_exterior_2014.jpg'
WHERE slug = 'studio-aalto';

UPDATE museums SET
    image_url         = 'https://upload.wikimedia.org/wikipedia/commons/8/83/Sederholmin_talo.jpg',
    image_url_large   = 'https://upload.wikimedia.org/wikipedia/commons/8/83/Sederholmin_talo.jpg',
    image_credit      = 'User Neofelis Nebulosa on fi.wikipedia',
    image_license     = 'Public domain',
    image_license_url = NULL,
    image_source_url  = 'https://commons.wikimedia.org/wiki/File:Sederholmin_talo.jpg'
WHERE slug = 'sederholm-house';

