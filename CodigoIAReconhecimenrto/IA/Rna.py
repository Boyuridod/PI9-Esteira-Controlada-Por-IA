from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from keras.layers import BatchNormalization
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from keras.preprocessing.image import load_img, img_to_array

import os
import numpy as np

#Dimencionamento das informações para rede
dim=120
filtros=20
canais=3
maskaraconvolucao=3
maskarapooling=2

#Convolucionamento das imagens
classificador = Sequential()
classificador.add(Conv2D(filtros, (maskaraconvolucao,maskaraconvolucao), input_shape = (dim, dim, canais), activation = 'relu'))
classificador.add(BatchNormalization())
classificador.add(MaxPooling2D(pool_size = (maskarapooling,maskarapooling)))

classificador.add(Conv2D(filtros, (maskaraconvolucao,maskaraconvolucao), activation = 'relu'))
classificador.add(BatchNormalization())
classificador.add(MaxPooling2D(pool_size = (maskarapooling,maskarapooling)))

classificador.add(Conv2D(filtros, (maskaraconvolucao,maskaraconvolucao), activation = 'relu'))
classificador.add(BatchNormalization())
classificador.add(MaxPooling2D(pool_size = (maskarapooling,maskarapooling)))

#Criação da rede
classificador.add(Flatten())

classificador.add(Dense(units = 128, activation = 'relu'))
classificador.add(Dense(units = 128, activation = 'relu'))
classificador.add(Dense(units = 2, activation = 'softmax'))

classificador.compile(optimizer = 'Adam', loss = 'categorical_crossentropy', 
                      metrics = ['categorical_accuracy'])



#Geração de mais dador de treinamento
dados_treinamento = ImageDataGenerator(rescale = 1./255,
                                       rotation_range = 7,
                                       horizontal_flip = True,
                                       shear_range = 0.2,
                                       height_shift_range = 0.07,
                                       zoom_range = 0.2)

dados_teste = ImageDataGenerator(rescale = 1./255)

#Especificação dos dados
os.chdir(r'D:\Imagens\Imagens')


amostras_treinamento = dados_treinamento.flow_from_directory('TreinamentoCaixa',
                                                             target_size = (dim, dim),
                                                             batch_size = 32,
                                                             class_mode = 'categorical')

amostras_teste = dados_teste.flow_from_directory('TesteCaixa',
                                                             target_size = (dim, dim),
                                                             batch_size = 32,
                                                             class_mode = 'categorical')

#Treinamento da rede
classificador.fit(amostras_treinamento, 
                            steps_per_epoch = 60, 
                            epochs = 10, 
                            validation_data = amostras_teste,
                            validation_steps = 60)

classificador.save('my_model.h5')

#imagem_teste = load_img('resultados1_2\subImage0.png', target_size = (120,120))
#imagem_teste = img_to_array(imagem_teste)
#imagem_teste /= 255
#imagem_teste = np.expand_dims(imagem_teste, axis = 0)
#previsao=classificador.predict(imagem_teste)
#print(previsao)

#imagem_teste = load_img('resultados1_2\subImage36.png', target_size = (120,120))
#imagem_teste = img_to_array(imagem_teste)
#imagem_teste /= 255
#imagem_teste = np.expand_dims(imagem_teste, axis = 0)
#previsao=classificador.predict(imagem_teste)
#print(previsao)
