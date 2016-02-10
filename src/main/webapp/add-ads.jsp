<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ru" ng-app="addAds">
<head>
    <meta charset="UTF-8">
    <title>Новое объявление</title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/add-ads.css">
</head>
<body>

<div ng-controller="addAdsController">

<!--Заголовок страницы-->
    <div class="page-header">
        <h1 style="padding-left: 40%">Новое объявление</h1>
    </div>

    <form class="form-horizontal" role="form" id="ads" name="ads" method="post" action="add-ads" enctype="multipart/form-data">
<!--Выбор категории-->
        <div class="form-group">
            <label for="productCategoryId" class="col-sm-2 control-label">Выберите категорию</label>
            <div class=" col-sm-10" ng-model="category1">
                <select ng-model="category" class="form-control" id="productCategoryId" required
                        ng-options="c.label for c in productCategory track by c.label" name="category" value="fuck">
                    <option disabled value="">--Выберите--</option>
                </select><br>
            </div>

<!--Выбор типа категории-->
            <span ng-show="category.categoryType">
                <div class="col-sm-offset-2 col-sm-10">
                    <select ng-model="typeCategory" name="typeCategory" class="form-control" id="catType" required
                            ng-options="t.label for t in category.categoryType track by t.label" name="categoryType">
                        <option disabled value="">--Выберите--</option>
                    </select><br>
                </div>
            </span>

<!--Выбор подтипа типа категории-->
            <span ng-show="typeCategory.typeType">
                <div class="col-sm-offset-2 col-sm-10">
                    <select ng-model="typeTypes" name="typeTypes" class="form-control" id="typType"
                            ng-options="t.label for t in typeCategory.typeType track by t.label">
                        <option disabled value="">--Выберите--</option>
                    </select><br>
                </div>
            </span>

<!--Выбор подподтип подтип типа категории-->
            <span ng-show="typeTypes.typeTypeType">
                <div class="col-sm-offset-2 col-sm-10">
                    <select ng-model="typeTypesType" name="typeTypesType" class="form-control" id="typTypeType"
                            ng-options="t.label for t in typeTypes.typeTypeType track by t.label">
                        <option disabled value="">--Выберите--</option>
                    </select><br>
                </div>
            </span>
        </div>

<!--Выбор города-->
        <div class="form-group">
            <label for="inputCity" class="col-sm-2 control-label">Местоположение</label>
            <div class="col-sm-10">
                <select ng-model="inputCity" name="city" class="form-control" id="inputCity" required
                        ng-options="c.label for c in cities track by c.label">
                    <option disabled value="">--Выберите--</option>
                </select><br>
            </div>
        </div>

<!--Ввод названия-->
        <div class="form-group">
            <label for="inputNameAds" class="col-sm-2 control-label">Название</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="adsName" id="inputNameAds" placeholder="Введите название" required value="Телефон">
            </div>
        </div>

<!--Ввод описания-->
        <div class="form-group">
            <label for="inputProductDescription" name="adsDescription" class="col-sm-2 control-label">Описание</label>
            <div class="col-sm-10 ">
                        <textarea id="inputProductDescription" class="form-control" name="adsDescription"></textarea>
            </div>
        </div>

<!--Цена-->
        <div class="form-group">
            <label for="inputPrice" class="col-sm-2 control-label">Цена</label>
            <div class="col-sm-10">
                <input type="number" class="form-control" id="inputPrice" placeholder="Введите цену в рублях"
                       required name="price" value="5000.00">
            </div>
        </div>

<!--Фото-->
        <div class="form-group">
            <label for="inputPhoto" class="col-sm-2 control-label">Фото</label>
            <div class="col-sm-10">
                    <span class="btn btn-default btn-file">
                        <i class="icon-plus"></i> <span>Добавить фото</span>
                        <input type="file" ng-model="photos" class="form-control" name="file" id="inputPhoto"
                               placeholder="Фото" multiple/>
                    </span>
                <div id="lists"></div>
            </div>
        </div>

<!--Отправка объявления-->
        <div class="form-group">
            <div class="col-sm-offset-7 col-sm-5">
                <button type="submit" class="btn btn-default" form="ads">Подать объявление</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
<script type="text/javascript" src="resources/scripts/angular.min.js"></script>
<script type="text/javascript" src="resources/scripts/add-ads.js"></script>
</body>
</html>
