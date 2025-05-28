import numpy as np
from keras.models import load_model
from keras.preprocessing.image import load_img, img_to_array

model = load_model('my_model.h5')

imagem_teste = load_img(r'D:\imagens\imagens\TesteCaixa\vermelho\download.png', target_size=(120, 120))

imagem_teste.show()

imagem_teste = img_to_array(imagem_teste)
imagem_teste /= 255.0 

imagem_teste = np.expand_dims(imagem_teste, axis=0)

previsao = model.predict(imagem_teste)

classe = np.argmax(previsao, axis=1)[0]

if classe == 0:
    print("verde") 
else:
    print("vermelho")
