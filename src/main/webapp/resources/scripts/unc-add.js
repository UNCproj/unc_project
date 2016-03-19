var addObject = angular.module('addObject',[]);
addObject.controller('addObjectController', function($scope){
    $scope.category = [
        {value : 'Transport', label : 'Транспорт',
            type : [
                {value :'Cars',label : 'Автомобили',
                    typesType : [
                        {value :'City',label : 'С пробегом '},
                        {value :'New',label : 'Новый'}
                    ]
                },
                {value :'Motorcycles and motor vehicles',label : 'Мотоциклы и мототехника',
                    typesType : [
                        {value :'Buggy',label : 'Багги'},
                        {value :'Off-road vehicles',label : 'Вездеходы'},
                        {value :'Karting',label : 'Картинг'},
                        {value :'ATVs',label : 'Квадроциклы'},
                        {value :'Mopeds and scooters',label : 'Мопеды и скутеры'},
                        {value :'Motorcycles',label : 'Мотоциклы',
                            typesTypesType: [
                                {value :'Travellers',label : 'Дорожные'},
                                {value :'Custom bikes',label : 'Кастом-байки'},
                                {value :'Cross and Enduro',label : 'Кросс и эндуро'},
                                {value :'Sporting',label : 'Спортивные'},
                                {value :'Choppers',label : 'Чопперы'}
                            ]
                        },
                        {value :'Snowmobiling',label : 'Снегоходы'}
                    ]
                },
                {value :'Trucks and buses',label : 'Грузовики и спецтехника',
                    typesType : [
                        {value :'Buses',label : 'Автобусы'},
                        {value :'Motorhomes',label : 'Автодома'},
                        {value :'cranes',label : 'Автокраны'},
                        {value :'Bulldozers',label : 'Бульдозеры'},
                        {value :'Trucks',label : 'Грузовики'},
                        {value :'Grounds Care',label : 'Коммунальная техника'},
                        {value :'Light trucks',label : 'Лёгкий транспорт'},
                        {value :'Forklifts',label : 'Погрузчики'},
                        {value :'Trailers',label : 'Прицепы'},
                        {value :'Farm Equipment',label : 'Сельхозтехника'},
                        {value :'Construction machinery',label : 'Строительная техника'},
                        {value :'Equipment for forestry',label : 'Техника для лесозаготовки'},
                        {value :'Tractors',label : 'Тягачи'},
                        {value :'Excavators',label : 'Экскаваторы'}
                    ]
                },
                {value :'Water transport',label : 'Водный транспорт',
                    typesType : [
                        {value :'Rowing boat',label : 'Вёсельные лодки'},
                        {value :'Watercraft',label : 'Гидроциклы'},
                        {value :'Boats and yachts',label : 'Катера и яхты'},
                        {value :'Kayaks and canoes',label : 'Каяки и каноэ'},
                        {value :'Motorboats',label : 'Моторные лодки'},
                        {value :'Inflatable boat',label : 'Надувные лодки'}
                    ]
                },
                {value :'Parts and accessories',label : 'Запчасти и аксессуары',
                    typesType : [
                        {value :'',label : 'Запчасти'},
                        {value :'',label : 'Аксессуары'},
                        {value :'',label : 'GPS-навигаторы'},
                        {value :'',label : 'Автокосметика и автохимия'},
                        {value :'',label : 'Аудио- и видеотехника'},
                        {value :'',label : 'Багажники и фаркопы'},
                        {value :'',label : 'Инструменты'},
                        {value :'',label : 'Прицепы'},
                        {value :'',label : 'Противоугонные устройства',
                            typesTypesType: [
                                {value :'Car Alarm',label : 'Автосигнализации'},
                                {value :'Immobilisers',label : 'Иммобилайзеры'},
                                {value :'Mechanical Locking',label : 'Механические блокираторы'},
                                {value :'Satellite systems',label : 'Спутниковые системы'}
                            ]
                        },
                        {value :'',label : 'Тюнинг'},
                        {value :'',label : 'Шины, диски и колёса',
                            typesTypesType: [
                                {value :'Bus',label : 'Шины'},
                                {value :'Motorcycle Tires',label : 'Мотошины'},
                                {value :'Disks',label : 'Диски'},
                                {value :'Wheels',label : 'Колёса'},
                                {value :'Covers',label : 'Колпаки'}
                            ]
                        },
                        {value :'',label : 'Экипировка'}
                    ]
                }
            ]
        },
        {value : 'Property', label : 'Недвижимость',
            type : [
                {value :'Apartments',label : 'Квартиры',
                    typesType: [
                        {value :'Selling',label : 'Продам',
                            typesTypesType: [
                                {value :'Resale',label : 'Вторичка'},
                                {value :'New building',label : 'Новостройка'}
                            ]
                        },
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю'},
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        }
                    ]
                },
                {value :'Rooms',label : 'Комнаты',
                    typesType: [
                        {value :'Selling',label : 'Продам'},
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю'},
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        }
                    ]
                },
                {value :'Houses, cottages',label : 'Дома, дачи, коттеджи',
                    typesType: [
                        {value :'Selling',label : 'Продам'},
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю'},
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'For a long time',label : 'На длительный срок'},
                                {value :'Daily',label : 'Посуточно'}
                            ]
                        }
                    ]
                },
                {value :'Land',label : 'Земельные участки',
                    typesType: [
                        {value :'Selling',label : 'Продам',
                            typesTypesType: [
                                {value :'Settlements (SGF)',label : 'Поселений (ИЖС)'},
                                {value :'Agricultural (SNT, NPD)',label : 'Сельхозназначения (СНТ, ДНП)'},
                                {value :'Promnaznacheniya',label : 'Промназначения'}
                            ]
                        },
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'Settlements (SGF)',label : 'Поселений (ИЖС)'},
                                {value :'Agricultural (SNT, NPD)',label : 'Сельхозназначения (СНТ, ДНП)'},
                                {value :'Promnaznacheniya',label : 'Промназначения'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю',
                            typesTypesType: [
                                {value :'Settlements (SGF)',label : 'Поселений (ИЖС)'},
                                {value :'Agricultural (SNT, NPD)',label : 'Сельхозназначения (СНТ, ДНП)'},
                                {value :'Promnaznacheniya',label : 'Промназначения'}
                            ]
                        },
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'Settlements (SGF)',label : 'Поселений (ИЖС)'},
                                {value :'Agricultural (SNT, NPD)',label : 'Сельхозназначения (СНТ, ДНП)'},
                                {value :'Promnaznacheniya',label : 'Промназначения'}
                            ]
                        }
                    ]
                },
                {value :'Garages and parking lots',label : 'Гаражи и машиноместа',
                    typesType: [
                        {value :'Selling',label : 'Продам',
                            typesTypesType: [
                                {value :'Garage',label : 'Гараж'},
                                {value :'Parking spaces',label : 'Машиноместо'}
                            ]
                        },
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'Garage',label : 'Гараж'},
                                {value :'Parking spaces',label : 'Машиноместо'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю',
                            typesTypesType: [
                                {value :'Garage',label : 'Гараж'},
                                {value :'Parking spaces',label : 'Машиноместо'}
                            ]
                        },
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'Garage',label : 'Гараж'},
                                {value :'Parking spaces',label : 'Машиноместо'}
                            ]
                        }
                    ]
                },
                {value :'Commercial property',label : 'Коммерческая недвижимость',
                    typesType: [
                        {value :'Selling',label : 'Продам',
                            typesTypesType: [
                                {value :'Hotel',label : 'Гостиница'},
                                {value :'Premise',label : 'Офисное помещение'},
                                {value :'The room catering',label : 'Помещение общественного питания'},
                                {value :'Placing free appointment',label : 'Помещение свободного назначения'},
                                {value :'Production room',label : 'Производственное помещение'},
                                {value :'Warehouse space',label : 'Складское помещение'},
                                {value :'Commercial premises',label : 'Торговое помещение'}
                            ]
                        },
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'Hotel',label : 'Гостиница'},
                                {value :'Premise',label : 'Офисное помещение'},
                                {value :'The room catering',label : 'Помещение общественного питания'},
                                {value :'Placing free appointment',label : 'Помещение свободного назначения'},
                                {value :'Production room',label : 'Производственное помещение'},
                                {value :'Warehouse space',label : 'Складское помещение'},
                                {value :'Commercial premises',label : 'Торговое помещение'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю',
                            typesTypesType: [
                                {value :'Hotel',label : 'Гостиница'},
                                {value :'Premise',label : 'Офисное помещение'},
                                {value :'The room catering',label : 'Помещение общественного питания'},
                                {value :'Placing free appointment',label : 'Помещение свободного назначения'},
                                {value :'Production room',label : 'Производственное помещение'},
                                {value :'Warehouse space',label : 'Складское помещение'},
                                {value :'Commercial premises',label : 'Торговое помещение'}
                            ]
                        },
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'Hotel',label : 'Гостиница'},
                                {value :'Premise',label : 'Офисное помещение'},
                                {value :'The room catering',label : 'Помещение общественного питания'},
                                {value :'Placing free appointment',label : 'Помещение свободного назначения'},
                                {value :'Production room',label : 'Производственное помещение'},
                                {value :'Warehouse space',label : 'Складское помещение'},
                                {value :'Commercial premises',label : 'Торговое помещение'}
                            ]
                        }
                    ]
                },
                {value :'Property Abroad',label : 'Недвижимость за рубежом',
                    typesType: [
                        {value :'Selling',label : 'Продам',
                            typesTypesType: [
                                {value :'Flats and apartments',label : 'Квартира, апартаменты'},
                                {value :'House, Villa',label : 'Дом, вилла'},
                                {value :'Land',label : 'Земельный участок'},
                                {value :'Garage parking place',label : 'Гараж, машиноместо'},
                                {value :'Commercial property',label : 'Коммерческая недвижимость'}
                            ]
                        },
                        {value :'Lease',label : 'Сдам',
                            typesTypesType: [
                                {value :'Flats and apartments',label : 'Квартира, апартаменты'},
                                {value :'House, Villa',label : 'Дом, вилла'},
                                {value :'Land',label : 'Земельный участок'},
                                {value :'Garage parking place',label : 'Гараж, машиноместо'},
                                {value :'Commercial property',label : 'Коммерческая недвижимость'}
                            ]
                        },
                        {value :'Marketplace',label : 'Куплю',
                            typesTypesType: [
                                {value :'Flats and apartments',label : 'Квартира, апартаменты'},
                                {value :'House, Villa',label : 'Дом, вилла'},
                                {value :'Land',label : 'Земельный участок'},
                                {value :'Garage parking place',label : 'Гараж, машиноместо'},
                                {value :'Commercial property',label : 'Коммерческая недвижимость'}
                            ]
                        },
                        {value :'Hire',label : 'Сниму',
                            typesTypesType: [
                                {value :'Flats and apartments',label : 'Квартира, апартаменты'},
                                {value :'House, Villa',label : 'Дом, вилла'},
                                {value :'Land',label : 'Земельный участок'},
                                {value :'Garage parking place',label : 'Гараж, машиноместо'},
                                {value :'Commercial property',label : 'Коммерческая недвижимость'}
                            ]
                        }
                    ]
                }
            ]
        },
        {value : 'Job', label : 'Работа',
            type : [
                {value :'Summary (job search)',label : 'Резюме (поиск работы)'}
            ]
        },
        {value : 'Services', label : 'Услуги',
            type : [
                {value :'Service offerings',label : 'Предложения услуг',
                    typesType: [
                        {value:'IT, Internet, telecommunications',label:'IT, интернет, телеком',
                            typesTypesType: [
                                {value :'Website development',label : 'Cоздание и продвижение сайтов'},
                                {value :'Jack of all cases',label : 'Мастер на все случаи'},
                                {value :'Setting up the Internet and networks',label : 'Настройка интернета и сетей'},
                                {value :'Installing and configuring the software',label : 'Установка и настройка ПО'}
                            ]
                        },
                        {value:'Domestic services',label:'Бытовые услуги',
                            typesTypesType: [
                                {value :'Production of keys',label : 'Изготовление ключей'},
                                {value :'Tailors and Services',label : 'Пошив и ремонт одежды'},
                                {value :'watch Repair',label : 'Ремонт часов'},
                                {value :'Dry cleaning, washing',label : 'Химчистка, стирка'},
                                {value :'Jewellery services',label : 'Ювелирные услуги'}
                            ]
                        },
                        {value:'Business services',label:'Деловые услуги',
                            typesTypesType: [
                                {value :'Accounting & Finance',label : 'Бухгалтерия, финансы'},
                                {value :'Counseling',label : 'Консультирование'},
                                {value :'Set and correction of text',label : 'Набор и коррекция текста'},
                                {value :'Transfer',label : 'Перевод'},
                                {value :'Legal services',label : 'Юридические услуги'}
                            ]
                        },
                        {value:'Art',label:'Искусство'},
                        {value:'Beauty, health',label:'Красота, здоровье'},
                        {value:'express orders',label:'Курьерские поручения'},
                        {value:'Master hour',label:'Мастер на час'},
                        {value:'Babysitters, nurses',label:'Няни, сиделки'},
                        {value:'Equipment manufacturing',label:'Оборудование, производство',
                            typesTypesType: [
                                {value :'Equipment rent',label : 'Аренда оборудования'},
                                {value :'Installation and maintenance of equipment',label : 'Монтаж и обслуживание оборудования'},
                                {value :'Production, processing',label : 'Производство, обработка'}
                            ]
                        },
                        {value:'Training courses',label:'Обучение, курсы'},
                        {value:'Protection, Security',label:'Охрана, безопасность'},
                        {value:'Catering, Catering',label:'Питание, кейтеринг'},
                        {value:'Holidays, events',label:'Праздники, мероприятия'},
                        {value:'Advertising, printing',label:'Реклама, полиграфия',
                            typesTypesType: [
                                {value :'Marketing, Advertising, PR',label : 'Маркетинг, реклама, PR'},
                                {value :'Printing design',label : 'Полиграфия, дизайн'}
                            ]
                        },
                        {value:'Repair and maintenance of machinery',label:'Ремонт и обслуживание техники',
                            typesTypesType: [
                                {value :'Televisions',label : 'Телевизоры'},
                                {value :'Gaming consoles',label : 'Игровые приставки'},
                                {value :'Computer technology',label : 'Компьютерная техника'},
                                {value :'Large home appliances',label : 'Крупная бытовая техника'},
                                {value :'Small Appliances',label : 'Мелкая бытовая техника'},
                                {value :'Mobile devices',label : 'Мобильные устройства'},
                                {value :'Photos, audio and video',label : 'Фото-, аудио-, видеотехника'}
                            ]
                        },
                        {value:'Repair, building',label:'Ремонт, строительство',
                            typesTypesType: [
                                {value :'Assembly and repair of furniture',label : 'Сборка и ремонт мебели'},
                                {value :'Finishing work',label : 'Отделочные работы'},
                                {value :'Electrics',label : 'Электрика'},
                                {value :'Plumbing',label : 'Сантехника'},
                                {value :'Repair of office',label : 'Ремонт офиса'},
                                {value :'Glazing of balconies',label : 'Остекление балконов'},
                                {value :'Repair of bathroom',label : 'Ремонт ванной'},
                                {value :'Construction of saunas',label : 'Строительство бань, саун'},
                                {value :'Kitchen Renovation',label : 'Ремонт кухни'},
                                {value :'Construction of houses, cottages',label : 'Строительство домов, коттеджей'},
                                {value :'Renovated apartment',label : 'Ремонт квартиры'}
                            ]
                        },
                        {value:'Garden, landscaping',label:'Сад, благоустройство'},
                        {value:'Transport, transportations',label:'Транспорт, перевозки',
                            typesTypesType: [
                                {value :'Car',label : 'Автосервис'},
                                {value :'Rent a Car',label : 'Аренда авто'},
                                {value :'Commercial freight',label : 'Коммерческие перевозки'},
                                {value :'Freight',label : 'Грузчики'},
                                {value :'Moving',label : 'Переезды'},
                                {value :'Special Equipment',label : 'Спецтехника'}
                            ]
                        },
                        {value:'Cleaning',label:'Уборка',
                            typesTypesType: [
                                {value :'Garbage removal',label : 'Вывоз мусора'},
                                {value :'Spring-cleaning',label : 'Генеральная уборка'},
                                {value :'Ironing',label : 'Глажка белья'},
                                {value :'Washing windows',label : 'Мойка окон'},
                                {value :'Easy cleaning',label : 'Простая уборка'},
                                {value :'Cleaning after repair',label : 'Уборка после ремонта'},
                                {value :'Carpet Cleaning',label : 'Чистка ковров'},
                                {value :'Cleaning upholstery',label : 'Чистка мягкой мебели'}
                            ]
                        },
                        {value:'Installation technology',label:'Установка техники'},
                        {value:'Caring for animals',label:'Уход за животными'},
                        {value:'Photo and video shooting',label:'Фото- и видеосъёмка'},
                        {value:'other',label:'Другое'}
                    ]
                },
                {value :'Request for services',label : 'Запросы на услуги',
                    typesType: [
                        {value:'IT, Internet, telecommunications',label:'IT, интернет, телеком',
                            typesTypesType: [
                                {value :'Website development',label : 'Cоздание и продвижение сайтов'},
                                {value :'Jack of all cases',label : 'Мастер на все случаи'},
                                {value :'Setting up the Internet and networks',label : 'Настройка интернета и сетей'},
                                {value :'Installing and configuring the software',label : 'Установка и настройка ПО'}
                            ]
                        },
                        {value:'Domestic services',label:'Бытовые услуги',
                            typesTypesType: [
                                {value :'Production of keys',label : 'Изготовление ключей'},
                                {value :'Tailors and Services',label : 'Пошив и ремонт одежды'},
                                {value :'watch Repair',label : 'Ремонт часов'},
                                {value :'Dry cleaning, washing',label : 'Химчистка, стирка'},
                                {value :'Jewellery services',label : 'Ювелирные услуги'}
                            ]
                        },
                        {value:'Business services',label:'Деловые услуги',
                            typesTypesType: [
                                {value :'Accounting & Finance',label : 'Бухгалтерия, финансы'},
                                {value :'Counseling',label : 'Консультирование'},
                                {value :'Set and correction of text',label : 'Набор и коррекция текста'},
                                {value :'Transfer',label : 'Перевод'},
                                {value :'Legal services',label : 'Юридические услуги'}
                            ]
                        },
                        {value:'Art',label:'Искусство'},
                        {value:'Beauty, health',label:'Красота, здоровье'},
                        {value:'express orders',label:'Курьерские поручения'},
                        {value:'Master hour',label:'Мастер на час'},
                        {value:'Babysitters, nurses',label:'Няни, сиделки'},
                        {value:'Equipment manufacturing',label:'Оборудование, производство',
                            typesTypesType: [
                                {value :'Equipment rent',label : 'Аренда оборудования'},
                                {value :'Installation and maintenance of equipment',label : 'Монтаж и обслуживание оборудования'},
                                {value :'Production, processing',label : 'Производство, обработка'}
                            ]
                        },
                        {value:'Training courses',label:'Обучение, курсы'},
                        {value:'Protection, Security',label:'Охрана, безопасность'},
                        {value:'Catering, Catering',label:'Питание, кейтеринг'},
                        {value:'Holidays, events',label:'Праздники, мероприятия'},
                        {value:'Advertising, printing',label:'Реклама, полиграфия',
                            typesTypesType: [
                                {value :'Marketing, Advertising, PR',label : 'Маркетинг, реклама, PR'},
                                {value :'Printing design',label : 'Полиграфия, дизайн'}
                            ]
                        },
                        {value:'Repair and maintenance of machinery',label:'Ремонт и обслуживание техники',
                            typesTypesType: [
                                {value :'Televisions',label : 'Телевизоры'},
                                {value :'Gaming consoles',label : 'Игровые приставки'},
                                {value :'Computer technology',label : 'Компьютерная техника'},
                                {value :'Large home appliances',label : 'Крупная бытовая техника'},
                                {value :'Small Appliances',label : 'Мелкая бытовая техника'},
                                {value :'Mobile devices',label : 'Мобильные устройства'},
                                {value :'Photos, audio and video',label : 'Фото-, аудио-, видеотехника'}
                            ]
                        },
                        {value:'Repair, building',label:'Ремонт, строительство',
                            typesTypesType: [
                                {value :'Assembly and repair of furniture',label : 'Сборка и ремонт мебели'},
                                {value :'Finishing work',label : 'Отделочные работы'},
                                {value :'Electrics',label : 'Электрика'},
                                {value :'Plumbing',label : 'Сантехника'},
                                {value :'Repair of office',label : 'Ремонт офиса'},
                                {value :'Glazing of balconies',label : 'Остекление балконов'},
                                {value :'Repair of bathroom',label : 'Ремонт ванной'},
                                {value :'Construction of saunas',label : 'Строительство бань, саун'},
                                {value :'Kitchen Renovation',label : 'Ремонт кухни'},
                                {value :'Construction of houses, cottages',label : 'Строительство домов, коттеджей'},
                                {value :'Renovated apartment',label : 'Ремонт квартиры'}
                            ]
                        },
                        {value:'Garden, landscaping',label:'Сад, благоустройство'},
                        {value:'Transport, transportations',label:'Транспорт, перевозки',
                            typesTypesType: [
                                {value :'Car',label : 'Автосервис'},
                                {value :'Rent a Car',label : 'Аренда авто'},
                                {value :'Commercial freight',label : 'Коммерческие перевозки'},
                                {value :'Freight',label : 'Грузчики'},
                                {value :'Moving',label : 'Переезды'},
                                {value :'Special Equipment',label : 'Спецтехника'}
                            ]
                        },
                        {value:'Cleaning',label:'Уборка',
                            typesTypesType: [
                                {value :'Garbage removal',label : 'Вывоз мусора'},
                                {value :'Spring-cleaning',label : 'Генеральная уборка'},
                                {value :'Ironing',label : 'Глажка белья'},
                                {value :'Washing windows',label : 'Мойка окон'},
                                {value :'Easy cleaning',label : 'Простая уборка'},
                                {value :'Cleaning after repair',label : 'Уборка после ремонта'},
                                {value :'Carpet Cleaning',label : 'Чистка ковров'},
                                {value :'Cleaning upholstery',label : 'Чистка мягкой мебели'}
                            ]
                        },
                        {value:'Installation technology',label:'Установка техники'},
                        {value:'Caring for animals',label:'Уход за животными'},
                        {value:'Photo and video shooting',label:'Фото- и видеосъёмка'},
                        {value:'other',label:'Другое'}
                    ]
                }
            ]
        },
        {value : 'Personal things', label : 'Личные вещи',
            type : [
                {value :'Clothes, shoes, accessories',label : 'Одежда, обувь, аксессуары'},
                {value :'Children clothing and shoes',label : 'Детская одежда и обувь'},
                {value :'Children goods and toys',label : 'Товары для детей и игрушки'},
                {value :'Watches & Jewellery',label : 'Часы и украшения'},
                {value :'Beauty and health',label : 'Красота и здоровье'}
            ]
        },
        {value : 'Home and garden', label : 'Для дома и дачи',
            type : [
                {value :'Appliances',label : 'Бытовая техника'},
                {value :'Furniture and interior',label : 'Мебель и интерьер',
                    typesType: [
                        {value:'Computer tables and chairs',label:'Компьютерные столы и кресла'},
                        {value:'Beds, sofas and armchairs',label:'Кровати, диваны и кресла'},
                        {value:'Kitchen sets',label:'Кухонные гарнитуры'},
                        {value:'Lighting',label:'Освещение'},
                        {value:'Cabinets',label:'Подставки и тумбы'},
                        {value:'Interior art',label:'Предметы интерьера, искусство'},
                        {value:'Tables and chairs',label:'Столы и стулья'},
                        {value:'Textiles and Carpets',label:'Текстиль и ковры'},
                        {value:'Cabinets and chests of drawers',label:'Шкафы и комоды'},
                        {value:'other',label:'Другое'}
                    ]
                },
                {value :'Dishes and products for the kitchen',label : 'Посуда и товары для кухни'},
                {value :'Food',label : 'Продукты питания'},
                {value :'Repair and construction',label : 'Ремонт и строительство'},
                {value :'Plants',label : 'Растения'}
            ]
        },
        {value : 'Consumer electronics', label : 'Бытовая электроника',
            type : [
                {value :'Audio and Video',label : 'Аудио и видео'},
                {value :'Games, consoles and software',label : 'Игры, приставки и программы'},
                {value :'Desktops',label : 'Настольные компьютеры'},
                {value :'Notebooks',label : 'Ноутбуки'},
                {value :'Office equipment and consumables',label : 'Оргтехника и расходники'},
                {value :'Tablets and e-books',label : 'Планшеты и электронные книги'},
                {value :'Phones',label : 'Телефоны'},
                {value :'Computer Hardware',label : 'Товары для компьютера'},
                {value :'Photographic',label : 'Фототехника'}
            ]
        },
        {value : 'Hobbies and leisure', label : 'Хобби и отдых',
            type : [
                {value :'Tickets & Travel',label : 'Билеты и путешествия'},
                {value :'Bicycles',label : 'Велосипеды'},
                {value :'Books and magazines',label : 'Книги и журналы'},
                {value :'Collecting',label : 'Коллекционирование'},
                {value :'Musical instruments',label : 'Музыкальные инструменты'},
                {value :'Hunting and fishing',label : 'Охота и рыбалка'},
                {value :'Sports and recreation',label : 'Спорт и отдых'}
            ]
        },
        {value : 'Animals', label : 'Животные',
            type : [
                {value :'Dogs',label : 'Собаки'},
                {value :'Cats',label : 'Кошки'},
                {value :'Birds',label : 'Птицы'},
                {value :'Aquarium',label : 'Аквариум'},
                {value :'Other animals',label : 'Другие животные'},
                {value :'Goods for pets',label : 'Товары для животных'}
            ]
        },
        {value : 'For business', label : 'Для бизнеса',
            type : [
                {value :'Ready business',label : 'Готовый бизнес'},
                {value :'Equipment for business',label : 'Оборудование для бизнеса'}
            ]
        }
    ];
    $scope.photos = {};
    $scope.cities = [
        {value:"Abakan",label:"Абакан"},
        {value:"Almetyevsk",label:"Альметьевск"},
        {value:"Anadyr",label:"Анадырь"},
        {value:"Anapa",label:"Анапа"},
        {value:"Arkhangelsk",label:"Архангельск"},
        {value:"Astrakhan",label:"Астрахань"},
        {value:"Barnaul",label:"Барнаул"},
        {value:"Belgorod",label:"Белгород"},
        {value:"Beslan",label:"Беслан"},
        {value:"Biysk",label:"Бийск"},
        {value:"Birobidzhan",label:"Биробиджан"},
        {value:"Blagoveshchensk",label:"Благовещенск"},
        {value:"Bologoye",label:"Бологое"},
        {value:"Bryansk",label:"Брянск"},
        {value:"Veliky Novgorod",label:"Великий Новгород"},
        {value:"Veliky Ustyug",label:"Великий Устюг"},
        {value:"Vladivostok",label:"Владивосток"},
        {value:"Vladikavkaz",label:"Владикавказ"},
        {value:"Vladimir",label:"Владимир"},
        {value:"Volgograd",label:"Волгоград"},
        {value:"Vologda",label:"Вологда"},
        {value:"Vorkuta",label:"Воркута"},
        {value:"Voronezh",label:"Воронеж"},
        {value:"Gatchina",label:"Гатчина"},
        {value:"Gdov",label:"Гдов"},
        {value:"Gelendzhik",label:"Геленджик"},
        {value:"Gorno-Altaysk",label:"Горно-Алтайск"},
        {value:"Grozny",label:"Грозный"},
        {value:"Gudermes",label:"Гудермес"},
        {value:"Gus-Khrustalny",label:"Гусь-Хрустальный"},
        {value:"Dzerzhinsk",label:"Дзержинск"},
        {value:"Dmitrov",label:"Дмитров"},
        {value:"Dubna",label:"Дубна"},
        {value:"Yeysk",label:"Ейск"},
        {value:"Yekaterinburg",label:"Екатеринбург"},
        {value:"Yelabuga",label:"Елабуга"},
        {value:"Yelets",label:"Елец"},
        {value:"Yessentuki",label:"Ессентуки"},
        {value:"Zlatoust",label:"Златоуст"},
        {value:"Ivanovo",label:"Иваново"},
        {value:"Izhevsk",label:"Ижевск"},
        {value:"Irkutsk",label:"Иркутск"},
        {value:"Yoshkar-Ola",label:"Йошкар-Ола"},
        {value:"Kazan",label:"Казань"},
        {value:"Kaliningrad",label:"Калининград"},
        {value:"Kaluga",label:"Калуга"},
        {value:"Kemerovo",label:"Кемерово"},
        {value:"Kislovodsk",label:"Кисловодск"},
        {value:"Komsomolsk-on-Amur",label:"Комсомольск-на-Амуре"},
        {value:"Kotlas",label:"Котлас"},
        {value:"Krasnodar",label:"Краснодар"},
        {value:"Krasnoyarsk",label:"Красноярск"},
        {value:"Kurgan",label:"Курган"},
        {value:"Kursk",label:"Курск"},
        {value:"Kyzyl",label:"Кызыл"},
        {value:"Leninogorsk",label:"Лениногорск"},
        {value:"Lensk",label:"Ленск"},
        {value:"Lipetsk",label:"Липецк"},
        {value:"Luga",label:"Луга"},
        {value:"Lyuban",label:"Любань"},
        {value:"Lyubertsy",label:"Люберцы"},
        {value:"Magadan",label:"Магадан"},
        {value:"Maykop",label:"Майкоп"},
        {value:"Makhachkala",label:"Махачкала"},
        {value:"Miass",label:"Миасс"},
        {value:"Mineralnye Vody",label:"Минеральные Воды"},
        {value:"Mirny",label:"Мирный"},
        {value:"Moscow",label:"Москва"},
        {value:"Murmansk",label:"Мурманск"},
        {value:"Murom",label:"Муром"},
        {value:"Mytishchi",label:"Мытищи"},
        {value:"Naberezhnye Chelny",label:"Набережные Челны"},
        {value:"Nadym",label:"Надым"},
        {value:"Nalchik",label:"Нальчик"},
        {value:"Nazran",label:"Назрань"},
        {value:"NakhodkaNaryan-Mar",label:"Нарьян-Мар"},
        {value:"Nakhodka",label:"Находка"},
        {value:"Nizhnevartovsk",label:"Нижневартовск"},
        {value:"Nizhnekamsk",label:"Нижнекамск"},
        {value:"Nizhny Novgorod",label:"Нижний Новгород"},
        {value:"Nizhny Tagil",label:"Нижний Тагил"},
        {value:"Novokuznetsk",label:"Новокузнецк"},
        {value:"Novosibirsk",label:"Новосибирск"},
        {value:"Novy Urengoy",label:"Новый Уренгой"},
        {value:"Norilsk",label:"Норильск"},
        {value:"Obninsk",label:"Обнинск"},
        {value:"Oktyabrsky",label:"Октябрьский"},
        {value:"Omsk",label:"Омск"},
        {value:"Orenburg",label:"Оренбург"},
        {value:"Orekhovo-Zuyevo",label:"Орехово-Зуево"},
        {value:"Oryol",label:"Орёл"},
        {value:"Penza",label:"Пенза"},
        {value:"Perm",label:"Пермь"},
        {value:"Petrozavodsk",label:"Петрозаводск"},
        {value:"Petropavlovsk-Kamchatsky",label:"Петропавловск-Камчатский"},
        {value:"Podolsk",label:"Подольск"},
        {value:"Pskov",label:"Псков"},
        {value:"Pyatigorsk",label:"Пятигорск"},
        {value:"Rostov-on-Don",label:"Ростов-на-Дону"},
        {value:"Rybinsk",label:"Рыбинск"},
        {value:"Ryazan",label:"Рязань"},
        {value:"Salekhard",label:"Салехард"},
        {value:"Samara",label:"Самара"},
        {value:"Saint Petersburg",label:"Санкт-Петербург"},
        {value:"Saransk",label:"Саранск"},
        {value:"Saratov",label:"Саратов"},
        {value:"Severodvinsk",label:"Северодвинск"},
        {value:"Smolensk",label:"Смоленск"},
        {value:"Sol-Iletsk",label:"Соль-Илецк"},
        {value:"Sochi",label:"Сочи"},
        {value:"Stavropol",label:"Ставрополь"},
        {value:"Surgut",label:"Сургут"},
        {value:"Syktyvkar",label:"Сыктывкар"},
        {value:"Tambov",label:"Тамбов"},
        {value:"Tver",label:"Тверь"},
        {value:"Tobolsk",label:"Тобольск"},
        {value:"Tolyatti",label:"Тольятти"},
        {value:"Tomsk",label:"Томск"},
        {value:"Tuapse",label:"Туапсе"},
        {value:"Tula",label:"Тула"},
        {value:"Tynda",label:"Тында"},
        {value:"Tyumen",label:"Тюмень"},
        {value:"Ulan-Ude",label:"Улан-Уде"},
        {value:"Ulyanovsk",label:"Ульяновск"},
        {value:"Ufa",label:"Уфа"},
        {value:"Khabarovsk",label:"Хабаровск"},
        {value:"Khanty-Mansiysk",label:"Ханты-Мансийск"},
        {value:"Chebarkul",label:"Чебаркуль"},
        {value:"Cheboksary",label:"Чебоксары"},
        {value:"Chelyabinsk",label:"Челябинск"},
        {value:"Cherepovets",label:"Череповец"},
        {value:"Cherkessk",label:"Черкесск"},
        {value:"Chistopol",label:"Чистополь"},
        {value:"Chita",label:"Чита"},
        {value:"Shadrinsk",label:"Шадринск"},
        {value:"Shatura",label:"Шатура"},
        {value:"Shuya",label:"Шуя"},
        {value:"Elista",label:"Элиста"},
        {value:"Engels",label:"Энгельс"},
        {value:"Yuzhno-Sakhalinsk",label:"Южно-Сахалинск"},
        {value:"Yakutsk",label:"Якутск"},
        {value:"Yaroslavl",label:"Ярославль"}
    ];
});
var showFile = (function () {
    var fr = new FileReader,
        i = 0,
        files, file;

    fr.onload = function (e) {
        var span;
        if (file.type.match('image.*')) {
            span = document.createElement('span');
            span.innerHTML = "<img src='" + e.target.result + "' class='styleImg'>";
            document.getElementById('lists').appendChild(span);
        }
        file = files[++i];
        if (file) {
            fr.readAsDataURL(file)
        } else {
            i = 0;
        }
    }

    return function (e) {
        files = e.target.files;
        file = files[i];
        if (files) {
            while (i < files.length && !file.type.match('image.*')) {
                file = files[++i];
            }
            if (file) fr.readAsDataURL(files[i])
        }
    }

})()

document.getElementById('inputPhoto').addEventListener('change', showFile, false);