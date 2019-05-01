/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataUpdateComponent } from 'app/entities/data/data-update.component';
import { DataService } from 'app/entities/data/data.service';
import { Data } from 'app/shared/model/data.model';

describe('Component Tests', () => {
    describe('Data Management Update Component', () => {
        let comp: DataUpdateComponent;
        let fixture: ComponentFixture<DataUpdateComponent>;
        let service: DataService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataUpdateComponent]
            })
                .overrideTemplate(DataUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Data(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.data = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Data();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.data = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
