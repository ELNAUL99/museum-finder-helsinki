-- Seed data: 30 real Helsinki museums.
-- Prices, hours and contact details are realistic 2025 snapshots for demo purposes;
-- always verify against the museum's own site before treating them as authoritative.
--
-- image_url is deliberately NULL: rather than ship stock photography that shows the wrong
-- building, the frontend draws a generated cover from the museum's name and themes. Fill
-- this column with licensed photography and the cover is replaced automatically.

INSERT INTO museums (slug, name, short_description, description, address, postal_code, district,
                     latitude, longitude, website, phone, image_url, adult_price_eur, free_entry,
                     free_entry_note, museum_card, wheelchair_accessible, family_friendly, has_cafe, has_shop) VALUES

('ateneum', 'Ateneum Art Museum',
 'Finland''s best-loved collection of national-romantic and classical art, from Gallen-Kallela to Schjerfbeck.',
 'Ateneum is the home of the Finnish National Gallery''s classic collection, covering art from the 18th century to the modernism of the 1960s. Its halls hold the works that shaped Finnish identity: Akseli Gallen-Kallela''s Kalevala paintings, Helene Schjerfbeck''s self-portraits, Hugo Simberg''s Wounded Angel and Albert Edelfelt''s luminous portraits, alongside works by Van Gogh, Cezanne and Munch.',
 'Kaivokatu 2', '00100', 'Kluuvi', 60.1704, 24.9442, 'https://ateneum.fi', '+358 294 500 401',
 NULL, 22.00, false, NULL, true, true, true, true, true),

('kiasma', 'Kiasma Museum of Contemporary Art',
 'Steven Holl''s curving landmark, filled with contemporary art, performance and new media.',
 'Kiasma shows contemporary art from Finland and abroad in a building that is an artwork in itself: Steven Holl''s 1998 design bends light through a curved gallery spine. The programme mixes large international names with young Finnish artists, and the ground floor theatre hosts performance, dance and live art all year.',
 'Mannerheiminaukio 2', '00100', 'Kamppi', 60.1709, 24.9360, 'https://kiasma.fi', '+358 294 500 501',
 NULL, 22.00, false, NULL, true, true, true, true, true),

('amos-rex', 'Amos Rex',
 'Underground galleries beneath a bubbling plaza, known for immersive digital and experimental art.',
 'Amos Rex buried its exhibition halls under Lasipalatsi Square, leaving skylight domes that children climb all summer. The museum is best known for large-scale immersive installations - teamLab, Bill Viola, Ryoji Ikeda - shown alongside the Sigurd Frosterus collection of European modernism and the restored 1930s Bio Rex cinema.',
 'Mannerheimintie 22-24', '00100', 'Kamppi', 60.1690, 24.9351, 'https://amosrex.fi', '+358 9 6844 460',
 NULL, 20.00, false, NULL, true, true, true, true, true),

('ham', 'HAM Helsinki Art Museum',
 'The city''s own art museum in Tennispalatsi, guardian of Helsinki''s 10,000-piece public art collection.',
 'HAM looks after the art the city of Helsinki owns - some 10,000 works, two thirds of them out in parks, metro stations and hospital corridors. Its Tennispalatsi galleries hold a permanent Tove Jansson room with her monumental 1940s frescoes, plus a changing programme of contemporary exhibitions.',
 'Eteläinen Rautatiekatu 8', '00100', 'Kamppi', 60.1683, 24.9316, 'https://hamhelsinki.fi', '+358 9 3108 7001',
 NULL, 20.00, false, NULL, true, true, true, true, true),

('design-museum', 'Design Museum Helsinki',
 'Finnish design from Aalto stools to Marimekko prints, in a neo-Gothic 1890s school building.',
 'The Design Museum tells the story of how a small northern country became a design superpower. The permanent exhibition Utopia Now runs from 19th-century craft through Alvar and Aino Aalto, Kaj Franck, Marimekko and Nokia to contemporary practice, and the changing shows cover fashion, industrial design and architecture.',
 'Korkeavuorenkatu 23', '00130', 'Kaartinkaupunki', 60.1637, 24.9482, 'https://designmuseum.fi', '+358 9 622 0540',
 NULL, 15.00, false, NULL, true, true, false, true, true),

('architecture-museum', 'Museum of Finnish Architecture',
 'Drawings, models and photographs charting Finland''s architectural century.',
 'Founded in 1956 as one of the world''s first architecture museums, it holds the drawings and models of Finland''s major architects and mounts exhibitions on how the country was built. It shares the block with the Design Museum, and a joint ticket covers both.',
 'Kasarmikatu 24', '00130', 'Kaartinkaupunki', 60.1642, 24.9494, 'https://mfa.fi', '+358 45 7731 0474',
 NULL, 12.00, false, NULL, true, true, false, false, true),

('national-museum', 'National Museum of Finland',
 'A thousand years of Finnish history inside Saarinen''s granite castle on Mannerheimintie.',
 'The National Museum covers Finland from the Stone Age to the present in a 1910 National Romantic building whose entrance hall carries Gallen-Kallela''s Kalevala ceiling frescoes. Highlights include the medieval church art, the Treasure Troves vault and a hands-on Workshop Vintti aimed squarely at children.',
 'Mannerheimintie 34', '00100', 'Etu-Töölö', 60.1758, 24.9314, 'https://kansallismuseo.fi', '+358 295 33 6000',
 NULL, 18.00, false, NULL, true, true, true, true, true),

('helsinki-city-museum', 'Helsinki City Museum',
 'Free museum of everyday Helsinki life, with a Children''s Town that families never want to leave.',
 'Five old buildings around Senate Square were joined into one free museum about how Helsinki people have actually lived. The Children''s Town lets kids run a 1930s shop and school, and the Time Machine puts you inside a 1950s living room. Admission is free, always.',
 'Aleksanterinkatu 16', '00170', 'Kruununhaka', 60.1686, 24.9520, 'https://helsinginkaupunginmuseo.fi', '+358 9 3103 6497',
 NULL, 0.00, true, 'Free admission for everyone, all year round.', false, true, true, true, true),

('natural-history-museum', 'Finnish Museum of Natural History (Luomus)',
 'Dinosaurs, a blue whale skeleton and the Finnish wilderness, inside a 1913 school.',
 'Luomus displays the university''s zoological and geological collections: the Bones and Skeletons hall, a full dinosaur gallery, the History of Life exhibition and Finnish Nature, where the country''s habitats are staged in dioramas from Lapland fells to Baltic shores. The elephant statue at the door is a Helsinki landmark.',
 'Pohjoinen Rautatiekatu 13', '00100', 'Etu-Töölö', 60.1730, 24.9310, 'https://luomus.fi', '+358 29 412 8800',
 NULL, 17.00, false, NULL, true, true, true, true, true),

('seurasaari', 'Seurasaari Open-Air Museum',
 'Farmsteads, a wooden church and squirrels, on an island of transplanted rural Finland.',
 'Eighty-seven historic wooden buildings were moved from across Finland onto Seurasaari island: a 1686 church, tarred farmhouses, a manor, saunas and a windmill, staffed in summer by guides in period dress. The island itself is a free public park, open year-round; the buildings and guided visits run in the summer season.',
 'Seurasaari', '00250', 'Seurasaari', 60.1856, 24.8829, 'https://kansallismuseo.fi/en/seurasaari', '+358 295 33 6912',
 NULL, 12.00, false, NULL, true, false, true, true, true),

('suomenlinna-museum', 'Suomenlinna Museum',
 'The sea fortress explained, on the UNESCO island a ferry ride from the Market Square.',
 'The main visitor centre museum on Suomenlinna tells 250 years of the fortress: Swedish Sveaborg, Russian garrison town, Finnish civil war prison camp, and now an inhabited island district. A short multi-screen film covers the arc, and the ticket includes the exhibition on the island''s builders.',
 'Suomenlinna C 74', '00190', 'Suomenlinna', 60.1454, 24.9881, 'https://suomenlinna.fi', '+358 295 338 410',
 NULL, 8.00, false, NULL, true, false, true, true, true),

('military-museum-maneesi', 'Military Museum''s Manege',
 'Tanks, artillery and armour in a Suomenlinna riding hall.',
 'The Manege holds the Military Museum''s heavy collection: Finnish and Soviet armour, artillery and anti-aircraft guns from the Winter and Continuation Wars, displayed in a converted Russian-era riding hall on Suomenlinna.',
 'Suomenlinna C 77', '00190', 'Suomenlinna', 60.1476, 24.9856, 'https://sotamuseo.fi', '+358 299 530 260',
 NULL, 8.00, false, NULL, true, false, true, false, false),

('sinebrychoff', 'Sinebrychoff Art Museum',
 'Old European masters in the Sinebrychoff family''s own 1840s home.',
 'Finland''s only museum devoted to old European art, in the empire-style home of the brewing family who collected it. Dutch and Flemish 17th-century painting, Swedish portraits, miniatures and the preserved period rooms of Paul and Fanny Sinebrychoff, left exactly as they arranged them.',
 'Bulevardi 40', '00120', 'Punavuori', 60.1637, 24.9317, 'https://sinebrychoffintaidemuseo.fi', '+358 294 500 460',
 NULL, 17.00, false, NULL, true, true, false, true, true),

('photography-museum', 'Finnish Museum of Photography',
 'The national photography museum in the Cable Factory''s raw concrete halls.',
 'K1 at Kaapelitehdas is the country''s main venue for photography, running documentary, contemporary and historical exhibitions plus a collection of over 4 million images. The industrial building it sits in - Nokia''s old cable works - is now Helsinki''s largest cultural centre.',
 'Tallberginkatu 1 G', '00180', 'Ruoholahti', 60.1620, 24.9058, 'https://valokuvataiteenmuseo.fi', '+358 9 6866 360',
 NULL, 12.00, false, NULL, true, true, false, true, true),

('hotel-restaurant-museum', 'Hotel and Restaurant Museum',
 'Menus, matchboxes and neon: the social history of Finnish eating out.',
 'A small, charming museum about how Finns have eaten and drunk in public - restaurant interiors, menus from state banquets, hotel keys, bar signage and the archives of the hospitality trade. Also at the Cable Factory.',
 'Tallberginkatu 1 G', '00180', 'Ruoholahti', 60.1620, 24.9058, 'https://hotellijaravintolamuseo.fi', '+358 9 6859 3700',
 NULL, 10.00, false, NULL, true, true, false, true, false),

('theatre-museum', 'Theatre Museum',
 'Costumes, sets and a stage you are allowed to walk onto.',
 'The Theatre Museum covers Finnish stage history through costumes, models, lighting rigs and recordings, and it is unusually hands-on: visitors can try the lighting desk, dress up and stand on a working stage set. Popular with families and school groups.',
 'Tallberginkatu 1 G', '00180', 'Ruoholahti', 60.1620, 24.9058, 'https://teatterimuseo.fi', '+358 9 5860 8500',
 NULL, 10.00, false, NULL, true, true, true, true, false),

('technology-museum', 'Helsinki Museum of Technology',
 'Industrial Finland in the city''s old waterworks by the Vantaa river rapids.',
 'Set in Helsinki''s first water treatment plant beside the Vanhankaupunginkoski rapids, the Museum of Technology covers Finnish industry, energy, telecoms and construction, with big machines indoors and a mill-race landscape outside that is worth the trip alone.',
 'Viikintie 1', '00560', 'Viikki', 60.2107, 25.0089, 'https://tekniikanmuseo.fi', '+358 9 7288 440',
 NULL, 12.00, false, NULL, true, true, true, true, true),

('tram-museum', 'Tram Museum',
 'Free museum of Helsinki''s trams, in the oldest surviving tram depot.',
 'Ratikkamuseo occupies the 1900 Töölö depot and shows how the horse tram became the green trams that still define Helsinki. Old carriages, driver''s cabs to sit in, ticket machines and route maps - and free entry, since it is part of the Helsinki City Museum.',
 'Töölönkatu 51', '00250', 'Taka-Töölö', 60.1830, 24.9223, 'https://helsinginkaupunginmuseo.fi', '+358 9 3103 6630',
 NULL, 0.00, true, 'Free, as part of the Helsinki City Museum.', false, true, true, false, false),

('villa-hakasalmi', 'Villa Hakasalmi',
 'A free city-museum villa behind the Finlandia Hall, with changing Helsinki exhibitions.',
 'An 1840s neo-renaissance villa in its own park beside Finlandia Hall, now a branch of the Helsinki City Museum showing changing exhibitions on the city''s cultural history. Free entry.',
 'Mannerheimintie 13 B', '00100', 'Etu-Töölö', 60.1746, 24.9332, 'https://helsinginkaupunginmuseo.fi', '+358 9 3103 6630',
 NULL, 0.00, true, 'Free admission.', false, true, false, false, false),

('burghers-house', 'Burgher''s House',
 'Helsinki''s oldest wooden house in the centre, furnished as a 1860s home.',
 'Ruiskumestarin talo, built in 1818, is the oldest wooden house still standing in central Helsinki. Inside, the rooms are set as the home of a fire master''s family in the 1860s, down to the coffee cups. Free, and a five-minute walk from the Market Square.',
 'Kristianinkatu 12', '00170', 'Kruununhaka', 60.1719, 24.9569, 'https://helsinginkaupunginmuseo.fi', '+358 9 3103 6630',
 NULL, 0.00, true, 'Free admission. Open in the summer season.', false, false, true, false, false),

('taidehalli', 'Kunsthalle Helsinki',
 'A 1928 classicist hall with no collection of its own - only changing exhibitions.',
 'Taidehalli has shown contemporary art, architecture and design since 1928 without owning a collection, which lets it move fast: a season might run from a Finnish painter''s retrospective to an international design biennial. The top-lit halls are among the best exhibition spaces in the city.',
 'Nervanderinkatu 3', '00100', 'Etu-Töölö', 60.1740, 24.9285, 'https://taidehalli.fi', '+358 9 454 2060',
 NULL, 14.00, false, NULL, true, true, false, true, true),

('didrichsen', 'Didrichsen Art Museum',
 'A modernist seaside villa on Kuusisaari holding modern art and pre-Columbian antiquities.',
 'Marie-Louise and Gunnar Didrichsen''s 1959 Viljo Revell villa is now a museum for their collection: Finnish modernism, Kandinsky, Leger, Miro and Picasso, plus pre-Columbian and Chinese antiquities, with Moore and Hepworth sculpture in the garden by the sea.',
 'Kuusilahdenkuja 1', '00340', 'Kuusisaari', 60.1867, 24.8607, 'https://didrichsenmuseum.fi', '+358 9 489 055',
 NULL, 14.00, false, NULL, true, true, false, true, true),

('villa-gyllenberg', 'Villa Gyllenberg',
 'A quiet Kuusisaari home museum with the best Helene Schjerfbeck room in the city.',
 'Ane and Signe Gyllenberg''s villa holds their collection of Finnish art - notably a superb group of Helene Schjerfbeck paintings - alongside old European works, in rooms kept as a private home with a view over the bay.',
 'Kuusisaarenpolku 11', '00340', 'Kuusisaari', 60.1888, 24.8654, 'https://gyllenberg.fi', '+358 50 5510 801',
 NULL, 12.00, false, NULL, true, false, false, true, false),

('mannerheim-museum', 'Mannerheim Museum',
 'The Marshal''s home in Kaivopuisto, left exactly as he lived in it.',
 'C.G.E. Mannerheim rented this Kaivopuisto villa from 1924 until his death, and it is preserved as he left it: hunting trophies from Asia, campaign furniture, his library and decorations. Visits are guided, which is what makes it good - the guides know the man''s contradictions.',
 'Kalliolinnantie 14', '00140', 'Kaivopuisto', 60.1571, 24.9576, 'https://mannerheim-museo.fi', '+358 9 635 443',
 NULL, 12.00, false, NULL, true, false, false, false, true),

('urho-kekkonen-museum', 'Urho Kekkonen Museum Tamminiemi',
 'The president''s official home on the Seurasaari shore, untouched since 1981.',
 'Tamminiemi was Urho Kekkonen''s residence through his 25 years as president, and the house - sauna included - stands as it was when he left it. Guided tours cover Cold War Finland from the inside: this is where Kekkonen negotiated in the sauna.',
 'Seurasaarentie 15', '00250', 'Seurasaari', 60.1878, 24.8843, 'https://kansallismuseo.fi', '+358 295 33 6900',
 NULL, 12.00, false, NULL, true, false, false, false, false),

('bank-of-finland-museum', 'Bank of Finland Museum',
 'Free museum of Finnish money, from sealskin currency to the euro.',
 'The Bank of Finland''s own museum explains monetary policy without putting you to sleep: the markka''s history, the 1990s banking crisis, the changeover to the euro, and a vault of banknotes and coins. Free, and central.',
 'Snellmaninkatu 2', '00170', 'Kruununhaka', 60.1707, 24.9530, 'https://suomenpankki.fi', '+358 9 1831',
 NULL, 0.00, true, 'Free admission.', false, true, false, false, false),

('helsinki-university-museum', 'Helsinki University Museum',
 'Free museum of academic life, medicine and student traditions in the Topelia block.',
 'The university''s own museum covers 380 years of teaching and research: medical instruments, teaching collections, the history of the student nations and the university''s move from Turku to Helsinki after the 1827 fire. Free entry.',
 'Fabianinkatu 33', '00170', 'Kruununhaka', 60.1697, 24.9490, 'https://helsinki.fi/en/helsinki-university-museum', '+358 2941 24608',
 NULL, 0.00, true, 'Free admission.', false, true, false, false, false),

('sports-museum', 'Sports Museum of Finland',
 'Finnish sporting history under the stands of the 1952 Olympic Stadium.',
 'Paavo Nurmi''s spikes, Lasse Viren''s medals and the story of the 1952 Helsinki Olympics, inside the renovated Olympic Stadium. The ticket pairs naturally with the stadium tower, which has the best free-standing view in the city.',
 'Paavo Nurmen kuja 1', '00250', 'Taka-Töölö', 60.1870, 24.9271, 'https://urheilumuseo.fi', '+358 9 434 2250',
 NULL, 10.00, false, NULL, true, true, true, true, true),

('observatory', 'Helsinki Observatory',
 'C.L. Engel''s 1834 observatory on Tähtitorninmäki, with the telescopes still in place.',
 'The University Observatory, designed by Engel and once among Europe''s most advanced, is now a museum of astronomy: the original refractors, the meridian room, and exhibitions on how Finland measured the sky. The hill it sits on is a park with a harbour view.',
 'Kopernikuksentie 1', '00130', 'Ullanlinna', 60.1611, 24.9503, 'https://luomus.fi', '+358 29 412 8800',
 NULL, 10.00, false, NULL, true, false, true, false, true),

('botanic-garden-greenhouses', 'Kaisaniemi Botanic Garden Greenhouses',
 'Ten climate zones under glass, two minutes from the Central Railway Station.',
 'The University of Helsinki''s Kaisaniemi greenhouses run from rainforest to desert across ten glasshouses, with a Victoria water lily pool that draws photographers every August. The outdoor garden around them is free and open all year.',
 'Kaisaniemenranta 2', '00100', 'Kluuvi', 60.1758, 24.9448, 'https://luomus.fi', '+358 29 412 8800',
 NULL, 9.00, false, NULL, true, true, true, true, true),

('cygnaeus-gallery', 'Cygnaeus Gallery',
 'Finland''s oldest public art gallery, a wooden seaside villa in Kaivopuisto.',
 'Fredrik Cygnaeus donated his villa and collection to the Finnish state in 1882, making this the country''s first public art gallery. Small, wooden, and full of 19th-century Finnish painting, with the sea directly outside the windows.',
 'Kalliolinnantie 8', '00140', 'Kaivopuisto', 60.1578, 24.9563, 'https://kansallisgalleria.fi', '+358 294 500 460',
 NULL, 10.00, false, NULL, true, false, false, false, false),

('aalto-house', 'The Aalto House',
 'Alvar and Aino Aalto''s own 1936 home and studio in Munkkiniemi, seen on a guided tour.',
 'The house Alvar and Aino Aalto built for themselves in Riihitie is a manifesto in brick, timber and white render - a family home with the practice attached. Visits are by guided tour only, and the furniture, textiles and lamps are the originals.',
 'Riihitie 20', '00330', 'Munkkiniemi', 60.1976, 24.8737, 'https://alvaraalto.fi', '+358 40 621 6764',
 NULL, 30.00, false, NULL, false, false, false, false, true),

('studio-aalto', 'Studio Aalto',
 'The architect''s 1955 office in Tiilimäki, with its amphitheatre courtyard.',
 'Aalto built this studio a short walk from his house when the practice outgrew the home office. The stepped drafting hall, the curved white wall and the garden amphitheatre where staff watched slide shows are all intact. Guided tours only.',
 'Tiilimäki 20', '00330', 'Munkkiniemi', 60.1993, 24.8748, 'https://alvaraalto.fi', '+358 40 621 6764',
 NULL, 20.00, false, NULL, false, false, false, false, true),

('sederholm-house', 'Sederholm House',
 'The oldest stone building in central Helsinki, now a free children''s history museum.',
 'Built in 1757 for merchant Johan Sederholm, this is the oldest building in downtown Helsinki. The Helsinki City Museum runs it as a free, hands-on history house for children, with 18th-century trades to try on the upper floors.',
 'Aleksanterinkatu 16-18', '00170', 'Kruununhaka', 60.1688, 24.9519, 'https://helsinginkaupunginmuseo.fi', '+358 9 3103 6630',
 NULL, 0.00, true, 'Free admission.', false, false, true, false, false);
