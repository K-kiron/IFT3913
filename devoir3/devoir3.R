#Devoir2 IFT3913
setwd("/Users/kironrothschild/Desktop/A2022/IFT3913/IFT3913_Git/devoir3")
#load("/Users/manpingli/Documents/ift3913/jfreechart-stats.csv")
jfreechart.stats <-read.csv("/Users/kironrothschild/Desktop/A2022/IFT3913/IFT3913_Git/devoir3/jfreechart-stats.csv")
#T1.visualisez
#DCP statistics
data = jfreechart.stats[,2:4]
DCP_u<-unname(quantile(data$DCP,0.75))
DCP_l<-unname(quantile(data$DCP,0.25))
DCP_d<-unname(DCP_u-DCP_l)
DCP_s<-unname(DCP_u+1.5*DCP_d)
DCP_i<-max(DCP_l-1.5*DCP_d,0)
DCP_m<-median(data$DCP)

#NCLOC statistics
NCLOC_u<-unname(quantile(data$NCLOC,0.75))
NCLOC_l<-unname(quantile(data$NCLOC,0.25))
NCLOC_d<-unname(NCLOC_u-NCLOC_l)
NCLOC_s<-unname(NCLOC_u+1.5*NCLOC_d)
NCLOC_i<-max(NCLOC_l-1.5*NCLOC_d,0)
NCLOC_m<-median(data$NCLOC)


#NOCom statistics
NOCom_u<-unname(quantile(data$NOCom,0.75))
NOCom_l<-unname(quantile(data$NOCom,0.25))
NOCom_d<-unname(NOCom_u-NOCom_l)
NOCom_s<-unname(NOCom_u+1.5*NOCom_d)
NOCom_i<-max(NOCom_l-1.5*NOCom_d,0)
NOCom_m<-median(data$NOCom)

#plot distribution
par(mfrow=c(3,1))
plot(density(data$DCP),main = "DCP density")
plot(density(data$NCLOC),main = "NCLOC density")
plot(density(data$NOCom),main = "NOCom density")


#boxplot
par(mfrow=c(3,1))
boxplot(data$DCP,data=data$DCP,horizontal = TRUE,main="DCP boxplot", outline = TRUE,ylab="DCP")
boxplot(data$NCLOC,data=data$NCLOC,horizontal = TRUE,main="NCLOC boxplot", outline = TRUE,ylab="NCLOC")
boxplot(data$NOCom,data=data$NOCom,horizontal = TRUE,main="NOCom boxplot", outline = TRUE,ylab="nombre de commits")


#T2.Etudiez les corrélations

#delete outliers
data2<-data[data$DCP<DCP_s,]
data2<-data2[data2$DCP>DCP_i,]
data2<-data2[data2$NCLOC<NCLOC_s,]
data2<-data2[data2$NCLOC>NCLOC_i,]
data2<-data2[data2$NOCom<NOCom_s,]
data2<-data2[data2$NOCom>NOCom_i,]

#NOCom vs NCLOC
corr_NOCom_NCLOC=cor(data2$NOCom,data2$NCLOC,method = "spearman")
linear_model1<-lm(data2$NCLOC~data2$NOCom)
par(mfrow=c(1,1))
plot( data2$NOCom,data2$NCLOC, xlab = "NOCom", ylab = "NCLOC", main = "Regression NCLOC sur NOCom(Sans points extrêmes)")
abline( linear_model1)
text(paste("Correlation:", round(cor(data2$NOCom, data2$NCLOC,method = "spearman"), 2)), x= 15, y = 100)
intercept1<-linear_model1$coefficients[1]#a
slope1<-linear_model1$coefficients[2]#b
text(paste("Intercept:", round(intercept1,2)), x= 15, y = 75)
text(paste("Slope:", round(slope1,2)), x= 15, y = 50)
summary(linear_model1)


#NOCom vs DCP
corr_NOCom_DCP=cor(data2$NOCom,data2$DCP,method = "spearman")
linear_model2<-lm(data2$DCP~data2$NOCom)
plot( data2$NOCom,data2$DCP, xlab = "NOCom", ylab = "DCP", main = "Regression DCP sur NOCom(Sans points extrêmes)")
abline( linear_model2)
text(paste("Correlation:", round(cor(data2$NOCom, data2$DCP,method = "spearman"), 2)), x= 12, y = 90)
intercept2<-linear_model2$coefficients[1]#a
slope2<-linear_model2$coefficients[2]#b
text(paste("Intercept:", round(intercept2,2)), x= 12, y = 85)
text(paste("Slope:", round(slope2,2)), x= 12, y = 80)
summary(linear_model2)

#T3. Quasi-experience
subdata.plus <- data[data$NOCom >= 10,]
subdata.moin <- data[data$NOCom < 10,]
t.test(subdata.plus$DCP,subdata.moin$DCP,alt="less",var.equal = TRUE)

